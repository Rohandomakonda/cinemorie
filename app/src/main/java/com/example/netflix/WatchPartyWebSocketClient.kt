// WatchPartyWebSocketClient.kt
package com.example.netflix.websocket

import android.annotation.SuppressLint
import android.util.Log
import com.example.netflix.WatchEvent
import com.google.gson.Gson
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

class WatchPartyWebSocketClient {
    private val gson = Gson()
    private val okHttpClient = OkHttpClient()
    private val stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8083/ws")

    private val _watchEvents = MutableSharedFlow<WatchEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val watchEvents: SharedFlow<WatchEvent> = _watchEvents

    private val _connectionStatus = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionStatus: SharedFlow<Boolean> = _connectionStatus

    @SuppressLint("CheckResult")
    fun connect() {
        stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("STOMP", "Connected to server")
                    _connectionStatus.tryEmit(true)
                    subscribeToUpdates()
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
    private fun subscribeToUpdates() {
        stompClient.topic("/topic/updates").subscribe { topicMessage ->
            try {
                val event = gson.fromJson(topicMessage.payload, WatchEvent::class.java)
                _watchEvents.tryEmit(event)
            } catch (e: Exception) {
                Log.e("STOMP", "Error parsing event: ${e.message}")
            }
        }
    }

    fun sendMessage(event: WatchEvent) {
        val json = gson.toJson(event)
        stompClient.send("/app/sync", json).subscribe()
    }

    fun disconnect() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
    }
}