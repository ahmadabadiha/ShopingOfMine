package com.example.shopingofmine.ui.options

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.datastore.OptionsDataStore
import com.example.shopingofmine.data.datastore.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(private val optionsDataStore: OptionsDataStore, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    val isDarkMode get() = savedStateHandle.get<Boolean>("darkMode")
    fun updateTheme(theme: Theme) = viewModelScope.launch {
        optionsDataStore.updateTheme(theme)
    }
}