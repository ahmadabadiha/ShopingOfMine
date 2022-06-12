package com.example.shopingofmine.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.Customer
import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    suspend fun createCustomer(customer: AppCustomer): StateFlow<ResultWrapper<Customer>> {
        return repository.createCustomer(customer).stateIn(viewModelScope)
    }
}