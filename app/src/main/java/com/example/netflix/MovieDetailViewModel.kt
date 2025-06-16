package com.example.netflix

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.netflix.UserPreferences.AuthPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movie: Movie?,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val watchPartyService = WatchPartyService()

    var showDialog by mutableStateOf(false)
        private set

    var inviteCode by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var createdPartyCode by mutableStateOf("")
        private set

    fun getMovie(): Movie? = movie

    fun toggleDialog(show: Boolean) {
        showDialog = show
        if (!show) {
            inviteCode = ""
            errorMessage = ""
            createdPartyCode = ""
        }
    }

    fun updateInviteCode(code: String) {
        inviteCode = code
        errorMessage = ""
    }

    fun createParty() {
        if (movie == null) {
            errorMessage = "Movie not found"
            return
        }

        isLoading = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val hostId = getCurrentUserId()
                val result = watchPartyService.createWatchParty(hostId, movie.id.toString())

                if (result != null) {
                    createdPartyCode = result.inviteCode
                    errorMessage = "Party created! Code: ${result.inviteCode}"
                } else {
                    errorMessage = "Failed to create party"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun joinParty() {
        if (inviteCode.isBlank()) {
            errorMessage = "Please enter an invite code"
            return
        }

        isLoading = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val userId = getCurrentUserId()
                val result = watchPartyService.joinWatchParty(inviteCode.trim(), userId)

                if (result != null) {
                    errorMessage = "Successfully joined party!"
                    createdPartyCode = inviteCode.trim()
                } else {
                    errorMessage = "Failed to join party. Check the invite code."
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun getCurrentUserId(): Long {
        return authPreferences.profileData.firstOrNull()?.id
            ?: throw IllegalStateException("User not logged in")
    }

    class Factory(
        private val movie: Movie?,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                val authPreferences = AuthPreferences(context.applicationContext)
                return MovieDetailViewModel(movie, authPreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
