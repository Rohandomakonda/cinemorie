package com.example.netflix.retrofit

import com.example.netflix.Movie
import retrofit2.http.GET

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.68.106:8765/")  // Or "http://localhost:8081/"
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val movieApi = retrofit.create(MovieApi::class.java)





interface MovieApi {
    @GET("/api/movies/allmovies")
    suspend fun getMovies(
        @retrofit2.http.Header("Authorization") token: String
    ): List<Movie>  // ✅ Return type fixed
}
