package com.example.netflix.UserPreferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.netflix.dtos.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object AuthKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val USER_ID = longPreferencesKey("user_id")
    val EMAIL = stringPreferencesKey("email")
    val NAME = stringPreferencesKey("name")
}

class AuthPreferences(private val context: Context) {

    suspend fun saveAuthResponse(auth: AuthResponse) {
        context.dataStore.edit { prefs ->
            prefs[AuthKeys.ACCESS_TOKEN] = auth.accessToken
            prefs[AuthKeys.REFRESH_TOKEN] = auth.refreshToken
            prefs[AuthKeys.USER_ID] = auth.id
            prefs[AuthKeys.EMAIL] = auth.email
            prefs[AuthKeys.NAME] = auth.name
        }
    }

    val authData: Flow<AuthResponse> = context.dataStore.data.map { prefs ->
        AuthResponse(
            accessToken = prefs[AuthKeys.ACCESS_TOKEN] ?: "",
            refreshToken = prefs[AuthKeys.REFRESH_TOKEN] ?: "",
            id = prefs[AuthKeys.USER_ID] ?: 0L,
            email = prefs[AuthKeys.EMAIL] ?: "",
            name = prefs[AuthKeys.NAME] ?: ""
        )
    }
}
