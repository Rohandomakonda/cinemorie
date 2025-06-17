package com.example.netflix

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.netflix.UserPreferences.AuthPreferences
import com.example.netflix.retrofit.watchlistApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WatchList(navController: NavController) {
//    val movies = listOf(
//        movieDetails(
//            title = "Action",
//            description = "Explosive stunts and thrilling scenes.",
//            tags = "Action",
//            rating = 7.8,
//            year = 2019,
//            ageRating = "13+",
//            backgroundImage = "https://www.tallengestore.com/cdn/shop/products/Fast_Furious_Presents_Hobbs_Shaw_-_Dwayne_Rock_Johnson_-_Jason_Statham_Idris_Alba_-_Tallenge_Hollywood_Action_Movie_Poster_dc2cfde0-101a-4bc4-a5c3-b469bc0c2fa8.jpg?v=1582543424",
//            genres = listOf("Action", "Adventure")
//        ),
//        movieDetails(
//            title = "Romance",
//            description = "A heartfelt story of love and longing.",
//            tags = "Romance",
//            rating = 8.2,
//            year = 2021,
//            ageRating = "13+",
//            backgroundImage = "https://m.media-amazon.com/images/M/MV5BZjE0ZjgzMzYtMTAxYi00NGMzLThmZDktNzFlMzA2MWRmYWQ0XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
//            genres = listOf("Romance", "Drama")
//        ),
//        movieDetails(
//            title = "Sci-fi",
//            description = "Parallel worlds and existential mysteries.",
//            tags = "Sci-fi",
//            rating = 7.9,
//            year = 2011,
//            ageRating = "13+",
//            backgroundImage = "https://www.indiewire.com/wp-content/uploads/2017/09/another-earth-2011.jpg?w=674",
//            genres = listOf("Sci-fi", "Mystery")
//        ),
//        movieDetails(
//            title = "Horror",
//            description = "A chilling tale to keep you up at night.",
//            tags = "Horror",
//            rating = 6.5,
//            year = 2016,
//            ageRating = "16+",
//            backgroundImage = "https://99designs-blog.imgix.net/blog/wp-content/uploads/2016/10/Dont-Speak.jpg?auto=format&q=60&fit=max&w=930",
//            genres = listOf("Horror", "Thriller")
//        ),
//        movieDetails(
//            title = "Comedy",
//            description = "Laugh-out-loud moments guaranteed.",
//            tags = "Comedy",
//            rating = 7.2,
//            year = 2009,
//            ageRating = "13+",
//            backgroundImage = "https://www.discountdisplays.co.uk/our-blog/wp-content/uploads/the-hangover-movie-poster.jpg",
//            genres = listOf("Comedy")
//        ),
//        movieDetails(
//            title = "Thriller",
//            description = "Dark twists and intense suspense.",
//            tags = "Thriller",
//            rating = 8.0,
//            year = 2014,
//            ageRating = "16+",
//            backgroundImage = "https://i.pinimg.com/736x/71/3c/bd/713cbd0590734a208fe5e8796715a6cf.jpg",
//            genres = listOf("Thriller", "Crime")
//        )
//    )
    val context = LocalContext.current
    val authPreferences = AuthPreferences(context)
    val profile by authPreferences.profileData.collectAsState(initial = null)
    val profileId = profile?.id
    val auth by authPreferences.authData.collectAsState(initial = null)
    val at = auth?.accessToken
    val isLoading = remember { mutableStateOf(true) }

    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    val removeFromWatchlist: (Long) -> Unit = { id ->
        movies = movies.filter { it.id.toLong() != id }
    }
    var series by remember { mutableStateOf<List<Series>>(emptyList()) }
    val removeFromSerieslist: (Long) -> Unit = { id ->
        series = series.filter { it.seriesId != id }
    }




    LaunchedEffect(profileId, at) {
        try {
            isLoading.value = true
            if (at != null && profileId != null) {
                val response = watchlistApi.getmovies(
                    token = at,
                    profileId = profileId
                )

                if (response.isSuccessful) {
                    movies = response.body() ?: emptyList()
                    isLoading.value = false
                    Toast.makeText(context, "Successfully fetched watchlist for movies", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Missing token or profile ID", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(profileId, at) {
        try {
            isLoading.value = true
            if (at != null && profileId != null) {
                val response = watchlistApi.getseries(
                    token = at,
                    profileId = profileId
                )

                if (response.isSuccessful) {
                    series = response.body() ?: emptyList()
                    isLoading.value = false
                    Toast.makeText(context, "Successfully fetched watchlist for series", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Missing token or profile ID", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }



    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading.value) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            item{
                Text("Movies")
            }
            items(movies) { movie ->
                if (at != null) {
                    if (profileId != null) {
                        watchlistcard(movie,navController,at,profileId,context,removeFromWatchlist)
                    }
                }
            }
        }
        item{
            Text("Series")
        }
        items(series) { serie ->
            if (at != null) {
                if (profileId != null) {
                    watchlistcardseries(serie,navController,at,profileId,context,removeFromSerieslist)
                }
            }
        }
    }



}

@Composable
fun watchlistcard(
    movieItem: Movie,
    navController: NavController,
    at:String, profileId: Long,
    context: Context,
    removeFromWatchlist: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(8.dp)
            // .border(BorderStroke(1.dp, Color.White)) // Correct usage
        )
        {
            // Movie Thumbnail
            Box(
                modifier = Modifier.clickable {
                    Log.d("MovieInfo", "Setting Movie: $movieItem")
                    // When you click on a movie item, navigate to MovieInfo
                    navController.currentBackStackEntry?.savedStateHandle?.set("movie", movieItem)
                    navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
                },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(movieItem.thumbnailUrl),
                    contentDescription = "image",
                    modifier = Modifier
                        .height(150.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray)
                        .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(modifier = Modifier.background(color = Color.Gray.copy(0.6f), CircleShape)) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Black), shape = CircleShape)
                            .size(40.dp),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Movie Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = movieItem.title,
                    fontSize = 18.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${movieItem.releaseDate} • ${movieItem.rating} ⭐ • ${movieItem.genre}",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Not Started",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray)
                ) {
                    // Simulate progress - example 60% done
                    val progress = 0.6f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)) // Purple gradient
                                )
                            )
                    )
                }


            }

            // Centered Remove Icon
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
                    .clickable {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {

                                if (at != null && profileId != null) {
                                    val response =
                                        watchlistApi.deletemoviewatchlist(
                                            token = at,
                                            userid = profileId,
                                            movieid = movieItem.id.toLong()
                                        )
                                    if (response != null) {
                                        if (response.isSuccessful) {

                                            removeFromWatchlist(movieItem.id.toLong())
                                            Toast.makeText(context, "Successfully deleted into watchlist ${movieItem.id}", Toast.LENGTH_SHORT).show()
                                            //navController.navigate(Screen.OtherPage.Profile.bRoute)
                                        } else {
                                            if (response != null) {
                                                Toast.makeText(context, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Missing token or profile ID", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    contentDescription = "Remove",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}
@Composable
fun watchlistcardseries(
    movieItem1: Series,
    navController: NavController,
    at:String, profileId: Long,
    context: Context,
    removeFromSerieslist: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(8.dp)
            // .border(BorderStroke(1.dp, Color.White)) // Correct usage
        )
        {
            // Movie Thumbnail
            Box(
                modifier = Modifier.clickable {
                    Log.d("MovieInfo", "Setting Movie: $movieItem1")
                    // When you click on a movie item, navigate to MovieInfo
                    navController.currentBackStackEntry?.savedStateHandle?.set("movie", movieItem1)
                    navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
                },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(movieItem1.thumbnailUrl),
                    contentDescription = "image",
                    modifier = Modifier
                        .height(150.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray)
                        .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(modifier = Modifier.background(color = Color.Gray.copy(0.6f), CircleShape)) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Black), shape = CircleShape)
                            .size(40.dp),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Movie Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = movieItem1.title,
                    fontSize = 18.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${movieItem1.releaseDate}",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Not Started",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray)
                ) {
                    // Simulate progress - example 60% done
                    val progress = 0.6f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)) // Purple gradient
                                )
                            )
                    )
                }


            }

            // Centered Remove Icon
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
                    .clickable {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {

                                if (at != null && profileId != null) {
                                    val response =
                                        watchlistApi.deletemoviewatchlist(
                                            token = at,
                                            userid = profileId,
                                            movieid = movieItem1.seriesId
                                        )
                                    if (response != null) {
                                        if (response.isSuccessful) {
                                            removeFromSerieslist(movieItem1.seriesId)


                                            Toast.makeText(context, "Successfully deleted into watchlist", Toast.LENGTH_SHORT).show()
                                            //navController.navigate(Screen.OtherPage.Profile.bRoute)
                                        } else {
                                            if (response != null) {
                                                Toast.makeText(context, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Missing token or profile ID", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    contentDescription = "Remove",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}