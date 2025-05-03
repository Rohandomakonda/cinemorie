package com.example.netflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.netflix.ui.theme.NetflixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetflixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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
                }
            }
        }
    }
}

