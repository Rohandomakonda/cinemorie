package com.example.netflix.retrofit

import com.example.netflix.Series
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val showsretrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8082/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()


val showApi = showsretrofit.create(ShowsApi::class.java)


interface ShowsApi{
    @GET("shows/allseries")
    suspend fun getShows(): List<Series>
}