package com.example.netflix


import androidx.annotation.DrawableRes
import com.example.netflix.Screen.BottomScreen.Home.icon

sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreen(
        val bTitle: String, val bRoute: String, @DrawableRes val icon: Int
    ):Screen(bTitle,bRoute){
        object Home : BottomScreen("Home", "home",R.drawable.baseline_home_24)

        object WatchList : BottomScreen(
            "WatchList", "watchlist", R.drawable.baseline_watch_later_24
        )
        object Shows: BottomScreen(
            "TV Shows", "tv_shows",R.drawable.baseline_tv_24

        )
        object Movies: BottomScreen(
            "Movies", "movies",R.drawable.baseline_movie_24

        )
    }





}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Movies,
    Screen.BottomScreen.Shows,
    Screen.BottomScreen.WatchList,
)

