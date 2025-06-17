package com.example.netflix.websocket

import android.annotation.SuppressLint
import android.util.Log
import com.example.netflix.WatchEvent
import com.google.gson.Gson
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

class WatchPartyWebSocketClient {

    private val gson = Gson()
    private val stompClient: StompClient = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,
        "ws://10.0.2.2:8084/ws" // Replace with your actual IP for real device testing
    )

    private val _watchEvents = MutableSharedFlow<WatchEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val watchEvents: SharedFlow<WatchEvent> = _watchEvents

    private val _participantUpdates = MutableSharedFlow<List<Long>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val participantUpdates: SharedFlow<List<Long>> = _participantUpdates

    private val _connectionStatus = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionStatus: SharedFlow<Boolean> = _connectionStatus

    private var currentPartyCode: String? = null

    @SuppressLint("CheckResult")
    fun connect(partyCode: String) {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }

        currentPartyCode = partyCode

        stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("STOMP", "Connected to server")
                    _connectionStatus.tryEmit(true)
                    subscribeToUpdates(partyCode)
                    subscribeToParticipants(partyCode)
                    sendParticipantRequest(partyCode)
                }

                LifecycleEvent.Type.CLOSED,
                LifecycleEvent.Type.ERROR -> {
                    Log.e("STOMP", "Connection error: ${event.exception}")
                    _connectionStatus.tryEmit(false)
                }

                else -> {}
            }
        }

        stompClient.connect()
    }

    @SuppressLint("CheckResult")
    private fun subscribeToUpdates(partyCode: String) {
        stompClient.topic("/topic/updates/$partyCode").subscribe { topicMessage ->
            try {
                val event = gson.fromJson(topicMessage.payload, WatchEvent::class.java)
                _watchEvents.tryEmit(event)
                Log.d("STOMP", "Received update: $event")
            } catch (e: Exception) {
                Log.e("STOMP", "Error parsing update: ${e.message}")
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeToParticipants(partyCode: String) {
        stompClient.topic("/topic/participants/$partyCode").subscribe { stompMessage ->
            try {
                val list = gson.fromJson(stompMessage.payload, Array<Long>::class.java).toList()
                _participantUpdates.tryEmit(list)
                Log.d("STOMP", "Received participants: $list")
            } catch (e: Exception) {
                Log.e("STOMP", "Error parsing participants: ${e.message}")
            }
        }
    }

    @SuppressLint("CheckResult")
    fun sendParticipantRequest(partyCode: String) {
        if (stompClient.isConnected) {
            stompClient.send("/app/participants/$partyCode", partyCode).subscribe(
                { Log.d("STOMP", "Requested participants for $partyCode") },
                { error -> Log.e("STOMP", "Error requesting participants: ${error.message}") }
            )
        }
    }

    fun sendMessage(event: WatchEvent) {
        if (!stompClient.isConnected) return
        val json = gson.toJson(event)
        val path = "/app/sync/${currentPartyCode ?: return}"
        stompClient.send(path, json).subscribe()
    }

    fun disconnect() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
    }
}
