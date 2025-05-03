package com.example.netflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
<<<<<<< HEAD

import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier

import androidx.navigation.compose.rememberNavController
=======
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
>>>>>>> 663e4a31aed0daadf810e37438c7ac9c4f956696
import com.example.netflix.ui.theme.NetflixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetflixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
<<<<<<< HEAD

                    Main()
=======
                   val data =  movieDetails(
                            title = "Harry Potter",
                            description = "blah blah",
                            tags = "A",
                            rating = 9.3,
                            year = 2001,
                            ageRating = "13+",
                            backgroundImage = "https://m.media-amazon.com/images/I/61wSaUwpR0L._AC_UF894,1000_QL80_.jpg",
                            castList = null,
                            genres = listOf("Magic","Mystery","Sci-fi")
                        )
                    MovieDetailScreen(data)
>>>>>>> 663e4a31aed0daadf810e37438c7ac9c4f956696
                }
            }
        }
    }
}

