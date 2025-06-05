package com.example.netflix.retrofit

import com.example.netflix.dtos.AuthResponse
import com.example.netflix.dtos.LoginRequest
import com.example.netflix.dtos.RegisterRequest
import com.example.netflix.dtos.VerificationRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


val registerretrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8090/")  // Or "http://localhost:8081/"
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val authApi = registerretrofit.create(AuthApi::class.java)

interface AuthApi{
    @POST("api/auth/register")  // or "/api/register" if your controller has @RequestMapping("/api")
     suspend fun register(@Body request: RegisterRequest): Response<Void>
    @POST("api/auth/verify")
    suspend fun verify(@Body request: VerificationRequest): Response<Void>
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    @POST("api/auth/google")
    suspend fun authenticateWithGoogle(@Body tokenMap: Map<String, String>): Response<AuthResponse>

}
