package com.example.shopingofmine.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.model.apimodels.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val optionsDataStore: OptionsDataStore) :
    ViewModel() {
    lateinit var productItem: ProductItem
    lateinit var order: Order
    lateinit var cartProducts: List<ProductItem>
    var countList: List<Int> = emptyList()
        set(value) {
            viewModelScope.launch {
                optionsDataStore.updateCartProductsCount(value.sum())
                field = value
            }
        }

    val cartProductsCount: Int
        get() {
            var value = 0
            viewModelScope.launch {
                runBlocking {
                    val preferencesInfo = optionsDataStore.preferences.take(1).first()
                    value = preferencesInfo.cartProductsCount
                }
            }
            return value
        }

    var customerId: Int? = null
        set(value) {
            if (value != null) {
                viewModelScope.launch {
                    optionsDataStore.updateCustomerId(value)
                    field = value
                }
            }
        }

    var notificationTimeInterval: Int? = null
        get() {
            var value: Int? = null
            viewModelScope.launch {
                runBlocking {
                    val preferencesInfo = optionsDataStore.preferences.take(1).first()
                    value = preferencesInfo.notificationInterval
                }
            }
            return value
        }
        set(value) {
            if (value != null) {
                viewModelScope.launch {
                    optionsDataStore.updateNotificationInterval(value)
                    field = value
                }
            }
        }
}