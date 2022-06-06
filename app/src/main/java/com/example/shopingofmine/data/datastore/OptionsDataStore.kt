package com.example.shopingofmine.data.datastore

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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
        private val Cart_Key = stringPreferencesKey("cart key")
    }

    private val dataStore = context.dataStore

    val preferences = dataStore.data.catch {
        Log.d("data store problem", it.message.toString())
    }.map { preferences ->
        val theme: Theme = Theme.valueOf(preferences[THEME_KEY] ?: Theme.AUTO.name)
        val cartIds: String = preferences[Cart_Key] ?: ""
        PreferencesInfo(theme, cartIds)
    }

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[THEME_KEY] = theme.name
        }
    }

    suspend fun updateCartItems(ids: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[Cart_Key] = ids
        }
    }

}
