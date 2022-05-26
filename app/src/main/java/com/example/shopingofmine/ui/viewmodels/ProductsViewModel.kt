package com.example.shopingofmine.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.model.Repository
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _products = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val products = _products.asStateFlow()

    fun getProducts(categoryId: String) = viewModelScope.launch {
        repository.getProductsByCategory(categoryId).collect{
            _products.emit(it)
        }
    }
}