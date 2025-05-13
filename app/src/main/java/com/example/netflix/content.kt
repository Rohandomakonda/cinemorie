package com.example.netflix

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ContentItem(val url: String, val title: String)
data class showDetails(
    val title: String,
    val description: String,
    val tags: String,
    val rating: Double,
    val year: Int,
    val ageRating: String,
    val backgroundImage: String,
    val episodes: List<Pair<String,Pair<String,String>>>? =null,
    val morelikethis: List<Pair<String,String>>? =null,
    val genres: List<String>
)

data class movieDetails(
    val title: String,
    val description: String?="",
    val tags: String?="",
    val rating: Double?=0.0,
    val year: Int?=0,
    val ageRating: String?="",
    val backgroundImage: String,
    val morelikethis: List<Pair<String,String>>? =null,
    val genres: List<String>?=null
)

@Parcelize
data class Movie(
    val id: Long,
    val title: String,
    val description: String,
    val releaseDate: String,
    val duration: Int,
    val genre: String,
    val language: String,
    val rating: Double,
     val videoData: String, // will not be passed
    val thumbnailUrl: String
) : Parcelable



