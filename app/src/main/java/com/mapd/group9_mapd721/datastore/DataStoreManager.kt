package com.example.bansidholakiya_mapd721_test.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class DataStoreManager(private val context: Context) {

    // Companion object to create a single instance of DataStore for username, email and id
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("hm_data_store")

        // Keys to uniquely identify username, email and id in DataStore
        val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        val USERNAME_KEY = stringPreferencesKey("username")
        val CNAME_KEY = stringPreferencesKey("customer_name")
    }

    suspend fun saveIsLogin(isLogin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = isLogin
        }
    }

    suspend fun readIsLogin(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_LOGIN_KEY] ?: return false
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun readUsername(): String {
        val preferences = context.dataStore.data.first()
        return preferences[USERNAME_KEY] ?: return ""
    }

    suspend fun saveCName(cName: String) {
        context.dataStore.edit { preferences ->
            preferences[CNAME_KEY] = cName
        }
    }

    suspend fun readCName(): String {
        val preferences = context.dataStore.data.first()
        return preferences[CNAME_KEY] ?: return ""
    }

    suspend fun clearDS() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

