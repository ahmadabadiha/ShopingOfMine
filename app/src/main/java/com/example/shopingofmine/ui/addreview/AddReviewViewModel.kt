package com.example.shopingofmine.ui.addreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var rating = 2

    suspend fun addReview(review: AppReview): StateFlow<Any> {
        return repository.addReview(review).stateIn(viewModelScope)
    }
}