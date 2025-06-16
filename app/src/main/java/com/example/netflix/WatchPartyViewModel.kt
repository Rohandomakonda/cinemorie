package com.example.netflix

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.netflix.UserPreferences.AuthPreferences
import com.example.netflix.websocket.WatchPartyWebSocketClient
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WatchPartyViewModel(
    private val partyCode: String,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val watchPartyService = WatchPartyService()
    private val webSocketClient = WatchPartyWebSocketClient()

    var party by mutableStateOf<WatchParty?>(null)
        private set

    var isHost by mutableStateOf(false)
        private set

    var isConnected by mutableStateOf(false)
        private set

    var participants by mutableStateOf<List<Long>>(emptyList())
        private set

    var currentTime by mutableStateOf(0.0)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf("")
        private set

    val watchEvents: SharedFlow<WatchEvent> = webSocketClient.watchEvents

    init {
        loadParty()
        observeConnectionStatus()
        observeParticipants()
    }
    private fun observeParticipants() {
        viewModelScope.launch {
            webSocketClient.participantUpdates.collect { updatedList ->
                participants = updatedList
            }
        }
    }


    private fun loadParty() {
        viewModelScope.launch {
            try {
                isLoading = true
                val result = watchPartyService.getWatchParty(partyCode)

                if (result != null) {
                    party = result
                    participants = result.participantIds
                    isHost = result.hostId == getCurrentUserId()
                    errorMessage = ""
                } else {
                    errorMessage = "Party not found"
                }
            } catch (e: Exception) {
                errorMessage = "Error loading party: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    fun connectToParty() {
        webSocketClient.connect(partyCode)
    }

    fun sendWatchEvent(action: String, timestamp: Double) {
        if (isHost && isConnected) {
            val event = WatchEvent(
                partyId = party?.id?.toString() ?: "",
                action = action,
                timestamp = timestamp
            )
            webSocketClient.sendMessage(event)

            // Update local state
            isPlaying = action == "play"
            currentTime = timestamp
        }
    }

    fun joinParty() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserId()
                val result = watchPartyService.joinWatchParty(partyCode, userId)

                if (result != null) {
                    party = result
                    participants = result.participantIds
                    errorMessage = ""

                    // After joining, request real-time update
                    webSocketClient.sendParticipantRequest(partyCode)
                } else {
                    errorMessage = "Failed to join party"
                }
            } catch (e: Exception) {
                errorMessage = "Error joining party: ${e.message}"
            }
        }
    }


    private fun observeConnectionStatus() {
        viewModelScope.launch {
            webSocketClient.connectionStatus.collect { connected ->
                isConnected = connected
            }
        }
    }

    fun disconnect() {
        webSocketClient.disconnect()
    }

    private suspend fun getCurrentUserId(): Long {
        return authPreferences.profileData.firstOrNull()?.id
            ?: throw IllegalStateException("User not logged in")
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect()
    }

    class Factory(
        private val partyCode: String,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WatchPartyViewModel::class.java)) {
                val authPreferences = AuthPreferences(context.applicationContext)
                return WatchPartyViewModel(partyCode, authPreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
