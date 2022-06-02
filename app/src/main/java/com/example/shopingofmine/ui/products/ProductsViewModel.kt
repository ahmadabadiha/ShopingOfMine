package com.example.shopingofmine.ui.products

import android.app.DownloadManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.model.Repository
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repository: Repository, private val savedStateHandle: SavedStateHandle) :
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

    fun getProducts(categoryId: String, orderBy: String = "date") = viewModelScope.launch {
        repository.getProductsByCategory(categoryId, orderBy).collectLatest {
            _categorizedProducts.emit(it)
        }
    }

    fun searchProducts(query: String, orderBy: String = "date") = viewModelScope.launch {
        repository.searchProducts(query, orderBy).collectLatest {
            _searchedResult.emit(it)
        }
    }
}