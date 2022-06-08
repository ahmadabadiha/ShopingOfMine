package com.example.shopingofmine.ui.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.Review
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val repository: Repository, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val productId get() = savedStateHandle.get<Int>("productId")

    suspend fun getProductReviews(): StateFlow<ResultWrapper<List<Review>>> {
        return repository.getProductReviews(arrayOf(productId!!), 100).stateIn(viewModelScope)
    }
}