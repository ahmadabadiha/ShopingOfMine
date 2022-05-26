package com.example.shopingofmine.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.model.Repository
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _popularProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val popularProducts = _popularProducts.asStateFlow()

    private val _topRatedProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val topRatedProducts = _topRatedProducts.asStateFlow()

    private val _newProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val newProducts = _newProducts.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts("popularity").collect {
                _popularProducts.emit(it)
            }
        }
        viewModelScope.launch {
            repository.getProducts("rating").collect {
                _topRatedProducts.emit(it)
            }
        }
        viewModelScope.launch {
            repository.getProducts("date").collect {
                _newProducts.emit(it)
            }
        }
    }
}