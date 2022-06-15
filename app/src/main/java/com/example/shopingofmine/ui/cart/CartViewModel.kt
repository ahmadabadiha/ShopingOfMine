package com.example.shopingofmine.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Customer
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.repository.Repository
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.appmodels.AppLineItem
import com.example.shopingofmine.data.model.appmodels.UpdateLineItem
import com.example.shopingofmine.data.model.appmodels.UpdateOrderClass
import com.example.shopingofmine.data.remote.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository, optionsDataStore: OptionsDataStore) : ViewModel() {

    private val preferences = optionsDataStore.preferences

    private val _cartProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val cartProducts = _cartProducts.asStateFlow()

    private val _errorInViewModelApiCalls = MutableSharedFlow<String>()
    val errorInViewModelApiCalls = _errorInViewModelApiCalls.asSharedFlow()

    var countList = mutableListOf<Int>()

    private lateinit var customer: Customer
    lateinit var order: Order

    fun getCustomerOrder() = viewModelScope.launch {
        val preferencesInfo = preferences.take(1).first()
        val customerId = preferencesInfo.customerId!!
            repository.getCustomerOrders(customerId).collectLatest {
                when (it) {
                    ResultWrapper.Loading -> {}
                    is ResultWrapper.Success -> {

                        val productIds = mutableListOf<Int>()
                        order = it.value[0]
                        val lineItems = order.line_items
                        if (lineItems.isNotEmpty()) {
                            lineItems.forEach {
                                productIds.add(it.product_id)
                            }
                            getCartProducts(productIds.toTypedArray())
                        }else  _errorInViewModelApiCalls.emit("cart empty")
                    }
                    is ResultWrapper.Error -> {
                        _errorInViewModelApiCalls.emit(it.message.toString())
                        Log.d("httperr", "getCustomerOrder: " + it.message)
                    }
                }
            }

    }

    private fun getCartProducts(productIds: Array<Int>) = viewModelScope.launch {
        repository.getProductByIds(productIds).collectLatest {
            if (it is ResultWrapper.Success) {

                val lineItems = order.line_items.toMutableList()
                if (lineItems.isNotEmpty()) {
                    val receivedIds = it.value.map {
                        it.id
                    }

                    countList = emptyList<Int>().toMutableList()
                    for (id in receivedIds) {
                        for (product in lineItems)
                            if (id == product.product_id) {
                                countList.add(product.quantity)
                            }
                    }
                    Log.d("ahmad", "getCartProducts: " + countList.size)
                    _cartProducts.emit(it)
                } else {
                    //todo show lottie for empty cart
                }
            }

        }
    }

    fun addToCart(addedProduct: ProductItem) = viewModelScope.launch {
        val preferencesInfo = preferences.take(1).first()
        val customerId = preferencesInfo.customerId
        if (customerId != null) {
            collectCustomer(customerId)
            collectCustomerOrderWithAddedProduct(customerId, addedProduct)
        } else Log.d("ahmad", "addToCart: customer null.")
    }

    private fun collectCustomer(customerId: Int) = viewModelScope.launch {
        repository.getCustomer(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    customer = it.value
                    Log.d("http", "addToCart: get customer success" + it.value)
                }
                is ResultWrapper.Error -> {
                    //todo
                    Log.d("httperr", "addToCart get customer err: " + it.message)
                }
            }
        }
    }

    private fun collectCustomerOrderWithAddedProduct(customerId: Int, addedProduct: ProductItem) = viewModelScope.launch {
        repository.getCustomerOrders(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    Log.d("http", "addToCart success: " + it.value)

                    val order = it.value[0]
                    val orderId = order.id
                    val orderProducts = order.line_items.toMutableList()
                    val orderProductIds = orderProducts.map { lineItem ->
                        lineItem.product_id
                    }
                    if (orderProductIds.contains(addedProduct.id)) {
                        val updatedProducts = orderProducts.map { lineItem ->
                            if (lineItem.product_id == addedProduct.id) UpdateLineItem(
                                lineItem.id,
                                lineItem.product_id,
                                lineItem.quantity + 1
                            )
                            else UpdateLineItem(lineItem.id, lineItem.product_id, lineItem.quantity)
                        }
                        val updatedOrder = UpdateOrderClass(updatedProducts)
                        collectUpdatedOrder(orderId, updatedOrder)
                    } else {
                        val updatedProducts: MutableList<Any> = orderProducts.map { lineItem ->
                            UpdateLineItem(lineItem.id, lineItem.product_id, lineItem.quantity)
                        }.toMutableList()
                        updatedProducts.add(AppLineItem(addedProduct.id, 1))
                        val updatedOrder = UpdateOrderClass(updatedProducts.toList())
                        collectUpdatedOrder(orderId, updatedOrder)
                    }
                }
                is ResultWrapper.Error -> {
                    //todo
                    Log.d("httperr", "addToCart: " + it.message)
                }
            }
        }
    }

    fun removeFromCart(removedProduct: ProductItem) = viewModelScope.launch { //todo make method shorter
        val preferencesInfo = preferences.take(1).first()
        val customerId = preferencesInfo.customerId

        if (customerId != null) {
            viewModelScope.launch {
                collectCustomer(customerId)
                collectCustomerOrderWithRemovedProduct(customerId, removedProduct)
            }
        } else Log.d("ahmad", "addToCart: customer null.")
    }

    private fun collectCustomerOrderWithRemovedProduct(customerId: Int, removedProduct: ProductItem) = viewModelScope.launch {
        repository.getCustomerOrders(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    Log.d("http", "addToCart success: " + it.value)
                    val order = it.value[0]
                    val orderId = order.id
                    val orderProducts = order.line_items.toMutableList()
                    val updatedProducts = orderProducts.map { lineItem ->
                        if (lineItem.product_id == removedProduct.id) UpdateLineItem(
                            lineItem.id,
                            lineItem.product_id,
                            lineItem.quantity - 1
                        )
                        else UpdateLineItem(lineItem.id, lineItem.product_id, lineItem.quantity)
                    }
                    val updatedOrder = UpdateOrderClass(updatedProducts)
                    collectUpdatedOrder(orderId, updatedOrder)
                }
                is ResultWrapper.Error -> {
                    //todo
                    Log.d("httperr", "addToCart: " + it.message)
                }
            }
        }
    }


    private fun collectUpdatedOrder(orderId: Int, updatedOrder: UpdateOrderClass) = viewModelScope.launch {
        repository.updateOrder(orderId, updatedOrder).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    getCustomerOrder()
                    //todo
                    Log.d("httpsuccess", "collect updated order: " + it.value)
                }
                is ResultWrapper.Error -> {
                    //todo
                    Log.d("httperr", "collect updated order: " + it.message)
                }
            }
        }
    }

    fun computePriceWithDiscount(cartItems: List<ProductItem>, productsCount: MutableList<Int>) =
        cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + productItem.price.toInt() * productsCount[index]
        }


    fun computeDiscount(cartItems: List<ProductItem>, productsCount: MutableList<Int>): Pair<Int, Int> {
        val discountAmount = cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + (productItem.regular_price.toInt() - productItem.price.toInt()) * productsCount[index]
        }

        val totalRegularPrice = cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + productItem.regular_price.toInt() * productsCount[index]
        }

        val discountPercent = ((discountAmount.toDouble() / totalRegularPrice.toDouble()) * 100).toInt()
        return Pair(discountAmount, discountPercent)
    }

    fun computePriceWithoutDiscount(cartItems: List<ProductItem>, productsCount: MutableList<Int>) =
        cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + productItem.regular_price.toInt() * productsCount[index]
        }
}