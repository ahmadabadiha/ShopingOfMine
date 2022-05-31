package com.example.shopingofmine.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import com.example.shopingofmine.model.serverdataclass.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    lateinit var productItem: ProductItem
    val cartItems = mutableMapOf<ProductItem, Int>()
}