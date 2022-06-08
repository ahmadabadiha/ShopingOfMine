package com.example.shopingofmine.ui.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.apimodels.Review
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    suspend fun getProductReviews(productId: Int, perPage: Int = 10): StateFlow<ResultWrapper<List<Review>>> {
        return repository.getProductReviews(arrayOf(productId), perPage).stateIn(viewModelScope)
    }
}