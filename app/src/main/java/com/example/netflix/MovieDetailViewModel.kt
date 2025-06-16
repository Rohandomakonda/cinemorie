package com.example.netflix

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val movie: Movie?) : ViewModel() {

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
            // Reset state when dialog is closed
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
                val hostId = getCurrentUserId() // You'll need to implement this
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
                    // Navigate to party screen
                    // You can add navigation logic here
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

    private fun getCurrentUserId(): Long {
        // TODO: Implement proper user session management
        // For now, return a mock user ID
        return 1L
    }

    class Factory(private val movie: Movie?) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                return MovieDetailViewModel(movie) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}