package com.uvg.lab12mia.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStorePrefs(
    private val dataStore: DataStore<Preferences>
) {
    private val usernameKey = stringPreferencesKey("username")

    suspend fun setUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[usernameKey] = username
        }
    }

    fun getUsername(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[usernameKey]
    }

    suspend fun clearUsername() {
        dataStore.edit { preferences ->
            preferences.remove(usernameKey)
        }
    }
}