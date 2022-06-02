package com.example.shopingofmine.ui.orderdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.model.Repository
import com.example.shopingofmine.model.localdataclass.LocalOrderClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun addOrder(order: LocalOrderClass) = viewModelScope.launch {
        repository.addOrder(order)
    }
}