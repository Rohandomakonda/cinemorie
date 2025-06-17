package com.example.netflix.retrofit

import com.example.netflix.Movie
import com.example.netflix.Series
import com.example.netflix.dtos.MoviesWatchlist
import com.example.netflix.dtos.SeriesWatchList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

val watchlistretrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8765/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()


val watchlistApi = watchlistretrofit.create(WatchlistApi::class.java)

interface WatchlistApi {
    @GET("api/watchlist/movies/{profileId}")
    suspend fun getmovies(
        @Header("Authorization") token: String,
        @Path("profileId") profileId: Long
    ): Response<List<Movie> >

    @FormUrlEncoded
    @POST("api/watchlist/movies")
    suspend fun addMovieToWatchlist(
        @Header("Authorization") token: String,
        @Field("id") id: Long?,             // nullable
        @Field("userid") userid: Long,
        @Field("movieid") movieid: Long
    ): Response<MoviesWatchlist>

    @DELETE("api/watchlist/movie/{id}")
    suspend fun deletemoviewatchlist(
        @Header("Authorization") token: String,
        @Field("userid") userid: Long,
        @Field("movieid") movieid: Long
    ): Response<Long>

    @GET("api/watchlist/series/{profileId}")
    suspend fun getseries(
        @Header("Authorization") token: String,
        @Path("profileId") profileId: Long
    ): Response<List<Series> >

    @FormUrlEncoded
    @POST("api/watchlist/series")
    suspend fun addserieswatchlist(
        @Header("Authorization") token: String,
        @Field("id") id: Long?,             // nullable
        @Field("userid") userid: Long,
        @Field("series") seriesid: Long
    ): Response<SeriesWatchList>

    @DELETE("api/watchlist/series/{id}")
    suspend fun deleteserieswatchlist(
        @Header("Authorization") token: String,
        @Field("userid") userid: Long,
        @Field("seriesid") movieid: Long
    ): Response<Long>


}
