package com.example.shopingofmine.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.repository.Repository
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository, private val optionsDataStore: OptionsDataStore) : ViewModel() {


    private val preferences = optionsDataStore.preferences

    private val _cartProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val cartProducts = _cartProducts.asStateFlow()


    var countList = mutableListOf<Int>()

    fun getCustomerOrder() = viewModelScope.launch {
        val preferencesInfo = preferences.take(1).first()
        val customerId = preferencesInfo.customerId
        if (customerId != null) {
            repository.getCustomerOrders(customerId).collectLatest {
                when (it) {
                    ResultWrapper.Loading -> {}
                    is ResultWrapper.Success -> {
                        if (it.value.isNotEmpty()) {
                            val productIds = mutableListOf<Int>()
                            val order = it.value[0]
                            order.line_items.forEach {
                                productIds.add(it.product_id)
                            }
                            getCartProducts(productIds.toTypedArray(), order)
                        } else {
                            //todo
                        }
                    }
                    is ResultWrapper.Error -> {
                        //todo
                        Log.d("httperr", "getCustomerOrder: " + it.message)
                    }
                }
            }
        } else {
            Log.d("ahmad", "getCustomerOrder: error: null customer id from data store")
        }
    }

    fun getCartProducts(productIds: Array<Int>, order: Order) = viewModelScope.launch {
        repository.getProductByIds(productIds).collectLatest {
            if (it is ResultWrapper.Success) {
                val receivedIds = it.value.map {
                    it.id
                }
                val lineItems = order.line_items.toMutableList()

                for (id in receivedIds) {
                    for (product in lineItems)
                        if (id == product.product_id) {
                            countList.add(product.quantity)
                        }
                }
                Log.d("ahmad", "getCartProducts: " + countList.size)

            }
            _cartProducts.emit(it)
        }
    }


    fun computePriceWithDiscount(cartItems: Map<ProductItem, Int>) =
        cartItems.map {
            it.key.price.toInt() * it.value
        }.sum()

    fun computeDiscount(cartItems: Map<ProductItem, Int>): Pair<Int, Int> {
        val discountAmount = cartItems.map {
            (it.key.regular_price.toInt() - it.key.price.toInt()) * it.value
        }.sum()
        val totalRegularPrice = cartItems.map {
            it.key.regular_price.toInt() * it.value
        }.sum()
        val discountPercent = ((discountAmount.toDouble() / totalRegularPrice.toDouble()) * 100).toInt()
        return Pair(discountAmount, discountPercent)
    }

    fun computePriceWithoutDiscount(cartItems: Map<ProductItem, Int>) =
        cartItems.map {
            it.key.regular_price.toInt() * it.value
        }.sum()
}