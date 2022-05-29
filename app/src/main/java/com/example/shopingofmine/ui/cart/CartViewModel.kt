package com.example.shopingofmine.ui.cart

import androidx.lifecycle.ViewModel
import com.example.shopingofmine.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(repository: Repository): ViewModel() {
}