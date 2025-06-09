package com.example.netflix.retrofit

import com.example.netflix.dtos.AuthResponse
import com.example.netflix.dtos.LoginRequest
import com.example.netflix.dtos.Profile
import com.example.netflix.dtos.ProfileRequest
import com.example.netflix.dtos.RegisterRequest
import com.example.netflix.dtos.VerificationRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


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
    @POST("api/auth/addprofile")
    suspend fun addprofile(@Body request: ProfileRequest): Response<Profile>
    @POST("api/auth/editprofile/{profileId}")
    suspend fun editprofile(@Path("profileId") profileId: Long, @Body request: ProfileRequest): Response<Profile>
    @POST("api/auth/getprofiles")
    suspend fun getprofiles(@Body userId: Long): Response<List<Profile>>

}
