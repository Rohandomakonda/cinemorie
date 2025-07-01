package com.example.netflix
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.netflix.UserPreferences.AuthPreferences


@Composable
fun GenrePage(navController: NavController) {
    val violetGradient = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }
    val genre = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<String>("genre")
    }

    // val isLoading by showViewModel.isLoading
    val context = LocalContext.current
    val authPreferences = AuthPreferences(context)
    val auth by authPreferences.authData.collectAsState(initial = null)
    auth?.let { authResponse ->
        val movieViewModel: MovieViewModel = viewModel(
            factory = MovieViewModelFactory(authResponse.accessToken)
        )
        val showViewModel: ShowViewModel = viewModel(
            factory = ShowViewModelFactory(authResponse.accessToken)
        )
        val filshows by showViewModel.shows
        val filmovies by movieViewModel.movies
        val movies = filmovies.filter { it.genre.equals(genre, ignoreCase = true) }
        val shows = filshows.filter { it.genres.equals(genre, ignoreCase = true) }
        val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
        val isLoading by movieViewModel.isLoading
        val isLoading1 by showViewModel.isLoading
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient(true, darkVioletColors))
                .padding(bottom = 16.dp) // give some space at the end
        ) {


            item{
                if(!isLoading1 && !isLoading){

                    card2(movies[0],navController)
                    Text(
                        text = "Top 10 in Cinémoire",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(movies) { Movie ->
                            MovieCard(Movie,navController)
                        }
                    }

                    Text(
                        text = "Top 10 in Cinémoire for series",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Log.d("UI1", "Loading is false, showing ${shows.size} movies")
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(shows) { Movie ->
                            ShowCard(Movie,navController)
                        }
                    }
                }
                else{
                    Spacer(modifier = Modifier.height(screenHeightDp/3))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }

                }
            }
        }


    }
}









