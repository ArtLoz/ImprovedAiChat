package com.shadow.deepseekimp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelper @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {


    suspend fun setTokenForDeepSeek(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_DEEP] = token
        }
    }

    suspend fun getTokenForDeepSeek(): String? {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_DEEP]
        }.firstOrNull()
    }

    suspend fun setTokenForQwen(token: String) {
        dataStore.edit { preferences ->
            preferences[QWEN_AUTH_TOKEN] = token
        }
    }

    suspend fun getTokenForQwen(): String? {
        return dataStore.data.map { preferences ->
            preferences[QWEN_AUTH_TOKEN]
        }.firstOrNull()
    }

    private companion object {
        val AUTH_TOKEN_DEEP = stringPreferencesKey("auth_token")
        val QWEN_AUTH_TOKEN = stringPreferencesKey("qwen_auth_token")
    }
}