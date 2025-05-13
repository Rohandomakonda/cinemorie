package com.example.netflix.retrofit

import com.example.netflix.Movie
import retrofit2.http.GET

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8081/")  // Or "http://localhost:8081/"
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val movieApi = retrofit.create(MovieApi::class.java)





interface MovieApi {
    @GET("movies/allmovies")
    suspend fun getMovies(): List<Movie>  // ✅ Return type fixed
}
