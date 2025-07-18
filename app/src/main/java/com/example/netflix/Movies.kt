package com.example.netflix
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.netflix.UserPreferences.AuthPreferences


@Composable
fun Movies(navController: NavController) {
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

    // val isLoading by showViewModel.isLoading
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val context = LocalContext.current
    val authPreferences = AuthPreferences(context)
    val auth by authPreferences.authData.collectAsState(initial = null)
    auth?.let { authResponse ->
        val movieViewModel: MovieViewModel = viewModel(
            factory = MovieViewModelFactory(authResponse.accessToken)
        )


        val movies by movieViewModel.movies
        val isLoading by movieViewModel.isLoading

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient(true, darkVioletColors))
                .padding(bottom = 16.dp) // give some space at the end
        ) {
//            item {
//                card2(ContentItem(url = "https://m.media-amazon.com/images/I/61wSaUwpR0L._AC_UF894,1000_QL80_.jpg", "Harry Potter"),navController)
//            }

            item{
                if( !isLoading){

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


