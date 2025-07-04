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
fun HomeScreen(navController: NavController) {
    val violetGradient = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }

    val genres = listOf(
        ContentItem(url = "https://www.tallengestore.com/cdn/shop/products/Fast_Furious_Presents_Hobbs_Shaw_-_Dwayne_Rock_Johnson_-_Jason_Statham_Idris_Alba_-_Tallenge_Hollywood_Action_Movie_Poster_dc2cfde0-101a-4bc4-a5c3-b469bc0c2fa8.jpg?v=1582543424", title = "Action"),
        ContentItem(url = "https://m.media-amazon.com/images/M/MV5BZjE0ZjgzMzYtMTAxYi00NGMzLThmZDktNzFlMzA2MWRmYWQ0XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg", title = "Romance"),
        ContentItem(url = "https://www.indiewire.com/wp-content/uploads/2017/09/another-earth-2011.jpg?w=674", title = "Sci-fi"),
        ContentItem(url = "https://99designs-blog.imgix.net/blog/wp-content/uploads/2016/10/Dont-Speak.jpg?auto=format&q=60&fit=max&w=930", title = "Horror"),
        ContentItem(url = "https://www.discountdisplays.co.uk/our-blog/wp-content/uploads/the-hangover-movie-poster.jpg", title = "Comedy"),
        ContentItem(url = "https://i.pinimg.com/736x/71/3c/bd/713cbd0590734a208fe5e8796715a6cf.jpg", title = "Thriller")
    )
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
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
        val shows by showViewModel.shows
        val movies by movieViewModel.movies
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
                        text = "Genre",
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
                        items(genres) {
                            Card1(it,navController)
                        }
                    }

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



@Composable
fun ShowCard(item: Series,navController: NavController) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable {
                Log.d("ShowInfo", "Setting Show: ${item.seasons[0].episodes}")
                // When you click on a movie item, navigate to MovieInfo
                navController.currentBackStackEntry?.savedStateHandle?.set("series", item)
                navController.navigate(Screen.OtherPage.ShowInfo.bRoute)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.thumbnailUrl),
            contentDescription = item.title,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
        )
    }
}
@Composable
fun MovieCard(item: Movie,navController: NavController) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable {
                Log.d("MovieInfo", "Setting Movie: $item")
                // When you click on a movie item, navigate to MovieInfo
                navController.currentBackStackEntry?.savedStateHandle?.set("movie", item)
                navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.thumbnailUrl),
            contentDescription = item.title,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
        )
    }
}

@Composable
fun Card1(item: ContentItem,navController: NavController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(200.dp)
    ) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable{
                    navController.currentBackStackEntry?.savedStateHandle?.set("genre", item.title)
                    navController.navigate(Screen.OtherPage.Genre.bRoute)}
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.url),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f)) // black/60 background
                    .padding(8.dp)
                    .clickable{}
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
@Composable
fun card2(item: Movie,navController: NavController) {
    val darkViolet = darken(Color(0xFF1E1B4B),0.3f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        // Full image display
        Image(
            painter = rememberAsyncImagePainter(item.thumbnailUrl),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Bottom gradient overlay to blend into background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, darkViolet)
                    )
                )
        )

        // Centered button over the bottom overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                ) {
                Button(
                    onClick = { navController.currentBackStackEntry?.savedStateHandle?.set("movie", item)
                        navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow, // Play Icon
                        contentDescription = "Play",
                        modifier = Modifier.size(24.dp), // Adjust icon size
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                    Text("Play", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("movie", item)
                        navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "More Info",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("More Info", color = Color.White)
                }
            }
        }
    }
}





