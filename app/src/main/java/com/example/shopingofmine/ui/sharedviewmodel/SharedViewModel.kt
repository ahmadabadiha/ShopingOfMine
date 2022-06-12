package com.example.shopingofmine.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository, private val optionsDataStore: OptionsDataStore) :
    ViewModel() {
    lateinit var productItem: ProductItem
    private val _cartItems = mutableMapOf<ProductItem, Int>()
    private val preferences = optionsDataStore.preferences
    val cartItems get() = _cartItems.toMap()
    var customerId: Int? = null
        set(value) {
            if (value != null) {
                viewModelScope.launch {
                    optionsDataStore.updateCustomerId(value)
                    field = value
                }
            }
        }

    var orderId: Int? = null

    fun updateOrder(order: AppOrderClass) = viewModelScope.launch {
        val x = repository.addOrder(order).collectLatest {

        }
    }

/*
    private fun updateCart() = viewModelScope.launch {
        val preferencesInfo = preferences.take(1).first()
        val idList = preferencesInfo.cartIds.split(",")
        val count = Collections.frequency(idList, productItem.id.toString())

    }

    fun addToCart(productItem: ProductItem) {

        viewModelScope.launch {
            val preferencesInfo = preferences.take(1).first()
            val ids = preferencesInfo.cartIds
            val updatedIds = ids + "${productItem.id},"
            Log.d("ahmad", "addToCart: " + updatedIds)

            optionsDataStore.updateCartItems(updatedIds)
            val idList = preferencesInfo.cartIds.split(",")

            val count = Collections.frequency(idList, productItem.id.toString())
            Log.d("ahmad", "addToCart: " + idList.toString() + " " + count.toString())

            _cartItems[productItem] = count
            Log.d("ahmad", "addToCart: " + _cartItems.size)
            Log.d("ahmad", "addToCart: " + cartItems.size)
        }
    }

    fun removeFromCart(productItem: ProductItem) {

        viewModelScope.launch {
            preferences.collectLatest { preferencesInfo ->
                val idList = preferencesInfo.cartIds.split(",").toMutableList()
                idList.remove(productItem.id.toString())
                val updatedList = idList.joinToString(",")
                optionsDataStore.updateCartItems(updatedList)
            }

            preferences.collectLatest { preferencesInfo ->
                val idList = preferencesInfo.cartIds.split(",")
                val count = Collections.frequency(idList, productItem.id)
                if (count == 0) _cartItems.remove(productItem)
                else _cartItems[productItem] = count
            }
            Log.d("ahmad", "addToCart: " + cartItems.toString())

        }
    }*/
}