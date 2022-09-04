package com.example.shopingofmine.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Coupon
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.appmodels.AppLineItem
import com.example.shopingofmine.data.model.appmodels.UpdatingLineItem
import com.example.shopingofmine.data.model.appmodels.UpdatingOrderClass
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository, optionsDataStore: OptionsDataStore) : ViewModel() {

    private val preferences = optionsDataStore.preferences

    var countList = mutableListOf<Int>()

    lateinit var order: Order

    private val _cartProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val cartProducts = _cartProducts.asStateFlow()

    private val _errorInViewModelApiCalls = MutableSharedFlow<String>()
    val errorInViewModelApiCalls = _errorInViewModelApiCalls.asSharedFlow()

    private val _coupon = MutableSharedFlow<ResultWrapper<List<Coupon>>>()
    val coupon = _coupon.asSharedFlow()

    fun getCoupon(code: String) = viewModelScope.launch {
        _coupon.emit(ResultWrapper.Loading)
        repository.getCoupon(code).collectLatest {
            _coupon.emit(it)
        }
    }

    fun getCustomerOrder() = viewModelScope.launch {
        val customerId = retrieveCustomerId()
        //todo cart fragment icon badge
        repository.getCustomerOrders(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    val productIds = mutableListOf<Int>()
                    if (it.value.isNotEmpty() && it.value[0].line_items.isNotEmpty()) {
                        order = it.value[0]
                        val lineItems = order.line_items
                        lineItems.forEach {
                            productIds.add(it.product_id)
                        }
                        getCartProducts(productIds.toTypedArray())
                    } else {
                        countList.clear()
                        _errorInViewModelApiCalls.emit("cart empty")
                    }
                }
                is ResultWrapper.Error -> {
                    _errorInViewModelApiCalls.emit(it.message.toString())
                }
            }
        }
    }

    private suspend fun retrieveCustomerId(): Int {
        val preferencesInfo = preferences.first()
        return preferencesInfo.customerId!!
    }

    private fun getCartProducts(productIds: Array<Int>) = viewModelScope.launch {
        repository.getProductByIds(productIds).collectLatest {

            if (it is ResultWrapper.Success) {
                val lineItems = order.line_items.toMutableList()
                val receivedIds = it.value.map {
                    it.id
                }
                countList.clear()
                for (id in receivedIds) {
                    for (product in lineItems)
                        if (id == product.product_id) {
                            countList.add(product.quantity)
                        }
                }
                _cartProducts.emit(it)
            }
            _cartProducts.emit(it)

        }
    }

    fun addToCart(addedProduct: ProductItem) = viewModelScope.launch {
        val customerId = retrieveCustomerId()
        collectCustomerOrderWithAddedProduct(customerId, addedProduct)
    }

    private fun collectCustomerOrderWithAddedProduct(customerId: Int, addedProduct: ProductItem) = viewModelScope.launch {
        repository.getCustomerOrders(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    val order = it.value[0]
                    val orderId = order.id
                    val orderProducts = order.line_items.toMutableList()
                    val orderProductIds = orderProducts.map { lineItem ->
                        lineItem.product_id
                    }
                    if (orderProductIds.contains(addedProduct.id)) {
                        val updatedProducts = orderProducts.map { lineItem ->
                            if (lineItem.product_id == addedProduct.id) UpdatingLineItem(
                                lineItem.id,
                                lineItem.product_id,
                                lineItem.quantity + 1
                            )
                            else UpdatingLineItem(lineItem.id, lineItem.product_id, lineItem.quantity)
                        }
                        val updatedOrder = UpdatingOrderClass(updatedProducts)
                        updateAndCollectOrder(orderId, updatedOrder)
                    } else {
                        val updatedProducts: MutableList<Any> = orderProducts.map { lineItem ->
                            UpdatingLineItem(lineItem.id, lineItem.product_id, lineItem.quantity)
                        }.toMutableList()
                        updatedProducts.add(AppLineItem(addedProduct.id, 1))
                        val updatedOrder = UpdatingOrderClass(updatedProducts.toList())
                        updateAndCollectOrder(orderId, updatedOrder)
                    }
                }
                is ResultWrapper.Error -> {
                    _errorInViewModelApiCalls.emit(it.message.toString())
                }
            }
        }
    }

    fun removeFromCart(removedProduct: ProductItem) = viewModelScope.launch { //todo make method shorter
        val customerId = retrieveCustomerId()
        collectCustomerOrderWithRemovedProduct(customerId, removedProduct)
    }

    private fun collectCustomerOrderWithRemovedProduct(customerId: Int, removedProduct: ProductItem) = viewModelScope.launch {
        repository.getCustomerOrders(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    val order = it.value[0]
                    val orderId = order.id
                    val orderProducts = order.line_items.toMutableList()
                    val updatedProducts = orderProducts.map { lineItem ->
                        if (lineItem.product_id == removedProduct.id)
                            UpdatingLineItem(
                                lineItem.id,
                                lineItem.product_id,
                                lineItem.quantity - 1
                            )
                        else UpdatingLineItem(lineItem.id, lineItem.product_id, lineItem.quantity)
                    }
                    val updatedOrder = UpdatingOrderClass(updatedProducts)
                    updateAndCollectOrder(orderId, updatedOrder)
                }
                is ResultWrapper.Error -> {
                    _errorInViewModelApiCalls.emit(it.message.toString())
                }
            }
        }
    }


    private fun updateAndCollectOrder(orderId: Int, updatedOrder: UpdatingOrderClass) = viewModelScope.launch {
        repository.updateOrder(orderId, updatedOrder).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    getCustomerOrder()
                }
                is ResultWrapper.Error -> {
                    _errorInViewModelApiCalls.emit(it.message.toString())
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