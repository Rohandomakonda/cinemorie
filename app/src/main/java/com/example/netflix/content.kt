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

@Parcelize
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
): Parcelable

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

@Parcelize
data class Series(
    val seriesId: Long,
    val title: String,
    val description: String,
    val releaseDate: String,
    val language: String,
    val genres: String,
    val thumbnailUrl: String,
    val seasons: ArrayList<Season> = arrayListOf()
) : Parcelable

@Parcelize
data class Season(
    val seasonid: Long,
    val seasonNumber: Int,
    val releaseDate: String,
    val episodeCount: Int,
    val episodes: ArrayList<Episode> = arrayListOf()
) : Parcelable

@Parcelize
data class Episode(
    val episodeId: Long,
    val episodeNumber: Int,
    val title: String,
    val duration: Int,
    val releaseDate: String,
    val fileUrl: String,
    val thumbnailUrl: String
) : Parcelable






