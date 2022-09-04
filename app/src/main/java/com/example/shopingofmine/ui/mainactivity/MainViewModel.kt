package com.example.shopingofmine.ui.mainactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(optionsDataStore: OptionsDataStore) : ViewModel() {
    val preferences = optionsDataStore.preferences

    private val _customerIsKnown = MutableSharedFlow<Boolean>()
    val customerIsKnown = _customerIsKnown.asSharedFlow()

    fun validateCustomerLogin() = viewModelScope.launch {
        val preferencesInfo = preferences.first()
        val customerId = preferencesInfo.customerId
        Log.d("ahmad", "validateCustomerLogin: ")
        if (customerId == null || customerId == -1) _customerIsKnown.emit(false)
        else _customerIsKnown.emit(true)
    }

}