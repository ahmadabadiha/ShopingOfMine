package com.example.shopingofmine.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.model.apimodels.Order
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
    lateinit var cartProducts: List<ProductItem>
    lateinit var countList: List<Int>
    lateinit var order: Order
    var customerId: Int? = null
        set(value) {
            if (value != null) {
                viewModelScope.launch {
                    optionsDataStore.updateCustomerId(value)
                    field = value
                }
            }
        }
}