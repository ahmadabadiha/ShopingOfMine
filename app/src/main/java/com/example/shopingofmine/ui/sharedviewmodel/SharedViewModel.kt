package com.example.shopingofmine.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.model.apimodels.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val optionsDataStore: OptionsDataStore) :
    ViewModel() {

    lateinit var productItem: ProductItem
    lateinit var order: Order
    lateinit var cartProducts: List<ProductItem>
    private val job by lazy { Job() }

    var countList = mutableListOf<Int>()
        set(value) {
            viewModelScope.launch {
                optionsDataStore.updateCartProductsCount(value.sum())
                field = value
            }
        }

    val cartProductsCount: Int
        get() {
            return runBlocking(job) {
                val preferencesInfo = optionsDataStore.preferences.first()
                preferencesInfo.cartProductsCount
            }
        }

    var customerId: Int? = null
        get() {
            return runBlocking(job) {
                val preferencesInfo = optionsDataStore.preferences.first()
                preferencesInfo.customerId
            }
        }
        set(value) {
            if (value != null) {
                viewModelScope.launch {
                    optionsDataStore.updateCustomerId(value)
                    field = value
                }
            } else field = -1
        }

    var notificationTimeInterval: Int? = null
        get() {
            return runBlocking(job) {
                val preferencesInfo = optionsDataStore.preferences.first()
                preferencesInfo.notificationInterval
            }

        }
        set(value) {
            if (value != null) {
                viewModelScope.launch {
                    optionsDataStore.updateNotificationInterval(value)
                    field = value
                }
            }
        }

    fun isProductItemInitialized() = ::productItem.isInitialized

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}