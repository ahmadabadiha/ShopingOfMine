package com.example.shopingofmine.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.model.Repository
import com.example.shopingofmine.model.serverdataclass.CategoryItem
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _categories = MutableStateFlow<ResultWrapper<List<CategoryItem>>>(ResultWrapper.Loading)
    val categories = _categories.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() = viewModelScope.launch {
        repository.getCategories().collect {
            _categories.emit(it)
        }
    }
}