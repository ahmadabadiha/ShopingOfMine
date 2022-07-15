package com.example.shopingofmine.ui.moreproducts

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.repository.Repository
import com.example.shopingofmine.ui.home.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreProductsViewModel @Inject constructor(private val repository: Repository, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val listType get() = savedStateHandle.get<Int>("listType")

    private val _loadedProduct = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val loadedProduct = _loadedProduct.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("ahmadabadi", "getProducts: " + listType.toString())
        when (listType) {
            ListType.POPULAR.ordinal -> repository.getProducts("popularity", "desc", 50).collectLatest { _loadedProduct.emit(it) }
            ListType.NEWEST.ordinal -> repository.getProducts("date", "desc", 50).collectLatest { _loadedProduct.emit(it) }
            ListType.TOP_RATED.ordinal -> repository.getProducts("rating", "desc", 50).collectLatest { _loadedProduct.emit(it) }
        }
    }
}