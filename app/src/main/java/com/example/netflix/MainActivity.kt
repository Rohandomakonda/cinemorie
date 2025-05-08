package com.example.netflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize


import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.netflix.ui.theme.NetflixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetflixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val controller: NavController = rememberNavController()
                    val navBackStackEntry by controller.currentBackStackEntryAsState()
                    val currentRoute =  Screen.BottomScreen.Home.bRoute
                    if (currentRoute !in introScreens.map { it.route }) {
                        Main()
                    }
                    else {
                        Navigation1(navController = controller)

                    }

                }
            }
        }
    }
}

@Composable
fun Navigation1(navController: NavController) {

    NavHost(navController = navController as NavHostController, startDestination = Screen.BottomScreen.Home.route) {
        composable(Screen.BottomScreen.Home.bRoute) {
            val data =  movieDetails(
                title = "Harry Potter",
                description = "blah blah",
                tags = "A",
                rating = 9.3,
                year = 2001,
                ageRating = "13+",
                backgroundImage = "https://m.media-amazon.com/images/I/61wSaUwpR0L._AC_UF894,1000_QL80_.jpg",
                morelikethis = listOf("Harry Potter" to "https://m.media-amazon.com/images/I/61wSaUwpR0L._AC_UF894,1000_QL80_.jpg","Google" to "google_icon.png"),
                genres = listOf("Magic","Mystery","Sci-fi")
            )
            //MovieDetailScreen(data)
            HomeScreen()
            //if u want to test test here



        }
        composable(Screen.BottomScreen.Movies.bRoute){

        }
        composable(Screen.BottomScreen.Shows.bRoute){

        }
        composable(Screen.BottomScreen.WatchList.bRoute){

        }



    }
}

