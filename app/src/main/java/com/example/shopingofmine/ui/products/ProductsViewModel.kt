package com.example.shopingofmine.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: com.example.shopingofmine.data.remote.repository.Repository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val categoryId get() = savedStateHandle.get<String>("category")
    private val query get() = savedStateHandle.get<String>("query")

    private val _categorizedProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val categorizedProducts = _categorizedProducts.asStateFlow()

    private val _searchedResult = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val searchedResult = _searchedResult.asStateFlow()


    init {
        categoryId?.let { getProducts(it) }
        query?.let { searchProducts(it) }
    }

    fun getProducts(categoryId: String, orderBy: String = "date", order: String = "desc") = viewModelScope.launch {
        repository.getProductsByCategory(categoryId, orderBy, order).collectLatest {
            _categorizedProducts.emit(it)
        }
    }

    fun searchProducts(query: String, orderBy: String = "date", order: String = "desc") = viewModelScope.launch {
        repository.searchProducts(query, orderBy, order).collectLatest {
            _searchedResult.emit(it)
        }
    }
}