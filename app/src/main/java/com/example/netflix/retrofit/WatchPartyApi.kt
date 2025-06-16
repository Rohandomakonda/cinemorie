package com.example.netflix

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class WatchParty(
    val id: Long,
    val hostId: Long,
    val videoId: String,
    val inviteCode: String,
    val createdAt: String,
    val participantIds: List<Long>
)

data class WatchEvent(
    val partyId: String,
    val action: String,
    val timestamp: Double
)

interface WatchPartyApi {
    @POST("api/watch-party/create")
    suspend fun createWatchParty(
        @Query("hostId") hostId: Long,
        @Query("videoId") videoId: String
    ): WatchParty

    @POST("api/watch-party/join")
    suspend fun joinWatchParty(
        @Query("inviteCode") inviteCode: String,
        @Query("userId") userId: Long
    ): WatchParty?

    @GET("api/watch-party/get")
    suspend fun getWatchParty(
        @Query("inviteCode") inviteCode: String
    ): WatchParty?

    @GET("api/watch-party/info")
    suspend fun getInfo(): String
}

class WatchPartyService {
    private val baseUrl = "http://10.0.2.2:8080/" // Use this for Android emulator
    // private val baseUrl = "http://localhost:8080/" // Use this for physical device on same network

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(WatchPartyApi::class.java)

    suspend fun createWatchParty(hostId: Long, videoId: String): WatchParty? {
        return withContext(Dispatchers.IO) {
            try {
                api.createWatchParty(hostId, videoId)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun joinWatchParty(inviteCode: String, userId: Long): WatchParty? {
        return withContext(Dispatchers.IO) {
            try {
                api.joinWatchParty(inviteCode, userId)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getWatchParty(inviteCode: String): WatchParty? {
        return withContext(Dispatchers.IO) {
            try {
                api.getWatchParty(inviteCode)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun testConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getInfo()
                response.contains("Watch Party API is up")
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}