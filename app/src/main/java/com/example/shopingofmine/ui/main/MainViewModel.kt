package com.example.shopingofmine.ui.main

import androidx.lifecycle.ViewModel
import com.example.shopingofmine.data.datastore.OptionsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val optionsDataStore: OptionsDataStore): ViewModel() {
    val preferences = optionsDataStore.preferences


}