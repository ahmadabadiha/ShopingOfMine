package com.example.shopingofmine.ui

import androidx.lifecycle.ViewModel
import com.example.shopingofmine.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

}