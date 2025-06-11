package com.example.netflix.UserPreferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.netflix.dtos.AuthResponse
import com.example.netflix.dtos.Profile
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object AuthKeys {
    val PROFILE_JSON = stringPreferencesKey("profile_json")
    val AUTH_JSON = stringPreferencesKey("auth_json")
}

class AuthPreferences(private val context: Context) {

    suspend fun saveAuthResponse(auth: AuthResponse) {
        val json = Gson().toJson(auth)
        context.dataStore.edit { prefs ->
            prefs[AuthKeys.AUTH_JSON] = json
        }
    }

    suspend fun saveProfile(profile: Profile) {
        val json = Gson().toJson(profile)
        context.dataStore.edit { prefs ->
            prefs[AuthKeys.PROFILE_JSON] = json
        }
    }

    val profileData: Flow<Profile?> = context.dataStore.data.map { prefs ->
        prefs[AuthKeys.PROFILE_JSON]?.let { Gson().fromJson(it, Profile::class.java) }
    }

    val authData: Flow<AuthResponse?> = context.dataStore.data.map { prefs ->
        prefs[AuthKeys.AUTH_JSON]?.let { Gson().fromJson(it, AuthResponse::class.java) }
    }
}
