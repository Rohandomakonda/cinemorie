package com.example.netflix

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WatchPartyWebSocketClient {
    private var webSocketClient: WebSocketClient? = null
    private val gson = Gson()

    private val _watchEvents = MutableSharedFlow<WatchEvent>()
    val watchEvents: SharedFlow<WatchEvent> = _watchEvents.asSharedFlow()

    private val _connectionStatus = MutableSharedFlow<Boolean>()
    val connectionStatus: SharedFlow<Boolean> = _connectionStatus.asSharedFlow()

    fun connect() {
        try {
            val uri = URI("ws://10.0.2.2:8080/ws") // For Android emulator
            // val uri = URI("ws://localhost:8080/ws") // For physical device

            webSocketClient = object : WebSocketClient(uri) {
                override fun onOpen(handshake: ServerHandshake?) {
                    Log.d("WebSocket", "Connected to server")
                    _connectionStatus.tryEmit(true)
                }

                override fun onMessage(message: String?) {
                    Log.d("WebSocket", "Received: $message")
                    message?.let {
                        try {
                            val watchEvent = gson.fromJson(it, WatchEvent::class.java)
                            _watchEvents.tryEmit(watchEvent)
                        } catch (e: Exception) {
                            Log.e("WebSocket", "Error parsing message: $it", e)
                        }
                    }
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    Log.d("WebSocket", "Connection closed: $reason")
                    _connectionStatus.tryEmit(false)
                }

                override fun onError(ex: Exception?) {
                    Log.e("WebSocket", "WebSocket error", ex)
                    _connectionStatus.tryEmit(false)
                }
            }

            webSocketClient?.connect()
        } catch (e: Exception) {
            Log.e("WebSocket", "Failed to connect", e)
            _connectionStatus.tryEmit(false)
        }
    }

    fun sendMessage(watchEvent: WatchEvent) {
        try {
            val message = gson.toJson(watchEvent)
            Log.d("WebSocket", "Sending: $message")
            webSocketClient?.send(message)
        } catch (e: Exception) {
            Log.e("WebSocket", "Failed to send message", e)
        }
    }

    fun disconnect() {
        webSocketClient?.close()
        webSocketClient = null
    }

    fun isConnected(): Boolean {
        return webSocketClient?.isOpen == true
    }
}