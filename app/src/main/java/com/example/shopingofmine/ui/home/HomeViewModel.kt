package com.example.shopingofmine.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    companion object {
        private val sliderProductIds = arrayOf(608)
    }

    private val _popularProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val popularProducts = _popularProducts.asStateFlow()

    private val _topRatedProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val topRatedProducts = _topRatedProducts.asStateFlow()

    private val _newProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val newProducts = _newProducts.asStateFlow()

    private val _sliderProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val sliderProducts = _sliderProducts.asStateFlow()

    init {
        getProducts()
        getSliderProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts("popularity", "desc", 10).collectLatest {
                _popularProducts.emit(it)
            }
        }
        viewModelScope.launch {
            repository.getProducts("rating", "desc", 10).collectLatest {
                _topRatedProducts.emit(it)
            }
        }
        viewModelScope.launch {
            repository.getProducts("date", "desc", 10).collectLatest {
                _newProducts.emit(it)
            }
        }
    }

    fun getSliderProducts() = viewModelScope.launch {
        repository.getProductByIds(sliderProductIds).collectLatest {
            _sliderProducts.emit(it)
        }
    }
}
