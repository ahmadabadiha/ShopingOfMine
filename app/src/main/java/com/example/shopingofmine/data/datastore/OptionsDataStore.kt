package com.example.shopingofmine.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
        private val CUSTOMER_ID = intPreferencesKey("customer key")
        private val NOTIFICATION_INTERVAL = intPreferencesKey("notification interval")
        private val CART_PRODUCTS_COUNT = intPreferencesKey("cart products count")
    }

    private val dataStore = context.dataStore

    val preferences = dataStore.data.catch {
        Log.d("data store problem", it.message.toString())
    }.map { preferences ->
        val theme: Theme = Theme.valueOf(preferences[THEME_KEY] ?: Theme.AUTO.name)
        val customerId = preferences[CUSTOMER_ID]
        val notificationInterval = preferences[NOTIFICATION_INTERVAL]
        val cartProductsCount = preferences[CART_PRODUCTS_COUNT]

        PreferencesInfo(theme, customerId, notificationInterval ?: 3, cartProductsCount ?: 0)
    }

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[THEME_KEY] = theme.name
        }
    }

    suspend fun updateCustomerId(id: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[CUSTOMER_ID] = id
        }
    }

    suspend fun updateNotificationInterval(interval: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[NOTIFICATION_INTERVAL] = interval
        }
    }

    suspend fun updateCartProductsCount(count: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[CART_PRODUCTS_COUNT] = count
        }
    }
}
