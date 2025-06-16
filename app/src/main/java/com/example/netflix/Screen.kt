package com.example.netflix


import androidx.annotation.DrawableRes

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
    sealed class OtherPage(
        val bTitle: String, val bRoute: String
    ):Screen(bTitle,bRoute){
        object Genre : OtherPage("Genre", "genre")
        object MovieInfo : OtherPage(
            "MovieInfo", "movieinfo"
        )
        object ShowInfo : OtherPage(
            "ShowInfo", "showinfo"
        )
        object Welcome: OtherPage(
            "Welcome", "welcome",
        )
        object Login: OtherPage(
            "Login", "login"
        )
        object Register: OtherPage(
            "Register", "register"
        )
        object Verification: OtherPage(
            "Verification", "verification"
        )
        object AddProfile: OtherPage(
            "AddProfile", "addprofile"
        )
        object EditProfile: OtherPage(
            "EditProfile", "editprofile"
        )
        object Profile: OtherPage(
            "Profile", "profile"
        )
        object Search: OtherPage(
            "Search","search"
        )
        object FullVideoScreen: OtherPage(
            "FullVideoScreen","fullScreenVideo"
        )
        object ShowFullScreenVideo: OtherPage(
            "ShowFullScreenVideo","showFullScreenVideo"
        )
        object WatchParty: OtherPage(
            "WatchParty","watchParty/{partyCode}"
        )

    }





}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Movies,
    Screen.BottomScreen.Shows,
    Screen.BottomScreen.WatchList,
)
val otherScreens = listOf(
    Screen.OtherPage.Genre,
    Screen.OtherPage.MovieInfo,
    Screen.OtherPage.ShowInfo
)
val introScreens= listOf(
    Screen.OtherPage.Welcome,
    Screen.OtherPage.Register,
    Screen.OtherPage.Login,
    Screen.OtherPage.Verification,
    Screen.OtherPage.AddProfile,
    Screen.OtherPage.EditProfile,
    Screen.OtherPage.Profile,
    Screen.OtherPage.Search,
    Screen.OtherPage.FullVideoScreen,
    Screen.OtherPage.ShowFullScreenVideo
)

