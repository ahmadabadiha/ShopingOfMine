package com.example.shopingofmine.ui.search

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
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _searchResult = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val searchResult = _searchResult.asStateFlow()

    fun searchProducts(query: String, orderBy: String = "date", order: String = "desc") = viewModelScope.launch {
        repository.searchProducts(query, orderBy, order).collectLatest {
            _searchResult.emit(it)
        }
    }
}