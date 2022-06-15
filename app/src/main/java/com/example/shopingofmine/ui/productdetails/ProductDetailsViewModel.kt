package com.example.shopingofmine.ui.productdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shopingofmine.data.NotificationWorker
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Customer
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.apimodels.Review
import com.example.shopingofmine.data.model.appmodels.*
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val optionsDataStore: OptionsDataStore,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId get() = savedStateHandle.get<Int>("productId")

    private val _customerIsKnown = MutableSharedFlow<Boolean>()
    val customerIsKnown = _customerIsKnown.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _review = MutableStateFlow<ResultWrapper<List<Review>>>(ResultWrapper.Loading)
    val review = _review.asStateFlow()

    private val _updatedOrder = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val updatedOrder = _updatedOrder.asStateFlow()

    var orderId: Int? = null

    private val preferences = optionsDataStore.preferences

    init {
        getProductReviews(productId!!)
    }

    private fun getProductReviews(productId: Int, perPage: Int = 10) = viewModelScope.launch {
        repository.getProductReviews(arrayOf(productId), perPage).collectLatest {
            _review.emit(it)
        }
    }

    private lateinit var customer: Customer

    private suspend fun addOrder(addedProduct: ProductItem) {
        val lineItem = AppLineItem(product_id = addedProduct.id, quantity = 1)
        val productsList = listOf<AppLineItem>(lineItem)
        val shipping = customer.shipping
        val appShipping = AppShipping(shipping.address_1, shipping.city, shipping.first_name, shipping.last_name, shipping.postcode)
        val order = AppOrderClass(customer.id, productsList, appShipping)
        repository.addOrder(order).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    val cartNotification = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                        .build()
                    addToCart(addedProduct)
                }
                is ResultWrapper.Error -> {
                    //todo
                    Log.d("httperr", "addOrder: " + it.message)
                }
            }
        }

    }

    fun addToCart(addedProduct: ProductItem) = viewModelScope.launch {
        val preferencesInfo = preferences.take(1).first()
        val customerId = preferencesInfo.customerId

        if (customerId != null) {
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
            collectCustomerOrderWithAddedProduct(customerId, addedProduct)
        } else _customerIsKnown.emit(false)
    }

    private fun collectCustomerOrderWithAddedProduct(customerId: Int, addedProduct: ProductItem) = viewModelScope.launch {
        repository.getCustomerOrders(customerId).collectLatest {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    Log.d("http", "addToCart success: " + it.value)
                    if (it.value.isNotEmpty()) {
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

                    } else addOrder(addedProduct)
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

                collectCustomerOrderWithRemovedProduct(customerId, removedProduct)
            }
        } else _customerIsKnown.emit(false)
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
           _updatedOrder.emit(it)
        }
    }

}