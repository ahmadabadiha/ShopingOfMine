package com.example.shopingofmine.data.datastore

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shopingofmine.data.model.apimodels.Customer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")

@Singleton
class OptionsDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme key")
        private val CUSTOMER_KEY = intPreferencesKey("customer key")
    }

    private val dataStore = context.dataStore

    val preferences = dataStore.data.catch {
        Log.d("data store problem", it.message.toString())
    }.map { preferences ->
        val theme: Theme = Theme.valueOf(preferences[THEME_KEY] ?: Theme.AUTO.name)
        val customerId = preferences[CUSTOMER_KEY]
        PreferencesInfo(theme, customerId)
    }

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[THEME_KEY] = theme.name
        }
    }

    suspend fun updateCustomerId(id: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[CUSTOMER_KEY] = id
        }
    }
}
