package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter



fun getClosestMatchesByLevenshtein(search: String, list: List<movieDetails>): List<movieDetails> {
    val filtered = list.filter {
        it.title.contains(search, ignoreCase = true)
    }

    return filtered

}

@Composable
fun Search(navController: NavController) {
    val violetGradient = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }

    var searchText by remember { mutableStateOf("") }
    var searchItemsFound by remember { mutableStateOf<List<movieDetails>>(emptyList()) }
    val allMovies = listOf(
        movieDetails(
            title = "Dune 2",
            backgroundImage = "https://www.tallengestore.com/cdn/shop/products/Fast_Furious_Presents_Hobbs_Shaw_-_Dwayne_Rock_Johnson_-_Jason_Statham_Idris_Alba_-_Tallenge_Hollywood_Action_Movie_Poster_dc2cfde0-101a-4bc4-a5c3-b469bc0c2fa8.jpg?v=1582543424",
        ),
        movieDetails(
            title = "Dune",
            backgroundImage = "https://m.media-amazon.com/images/M/MV5BZjE0ZjgzMzYtMTAxYi00NGMzLThmZDktNzFlMzA2MWRmYWQ0XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
        ),
        movieDetails(
            title = "Stranger Things",
            backgroundImage = "https://www.indiewire.com/wp-content/uploads/2017/09/another-earth-2011.jpg?w=674",
        ),
        movieDetails(
            title = "Big Bang Theory",
            backgroundImage = "https://www.discountdisplays.co.uk/our-blog/wp-content/uploads/the-hangover-movie-poster.jpg",
        ),
        movieDetails(
            title = "Dark",
            backgroundImage = "https://i.pinimg.com/736x/71/3c/bd/713cbd0590734a208fe5e8796715a6cf.jpg",
        )
    )


    searchItemsFound = getClosestMatchesByLevenshtein(searchText, allMovies)

    Box(modifier = Modifier.background(gradient(true, darkVioletColors))) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    searchItemsFound = getClosestMatchesByLevenshtein(it, allMovies)
                },
                label = { Text("Search for Movie, Shows..", color = Color.White) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30))
                    .width(50.dp)
                    .padding(top=40.dp),
                shape = RoundedCornerShape(100)
            )
            if(searchItemsFound.equals(null)){
                Text("No Results Found")
            }
            else{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier,
                    state = rememberLazyGridState(),
                    contentPadding = PaddingValues(8.dp),
                    reverseLayout = false,
                    verticalArrangement = Arrangement.Top,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    userScrollEnabled = true
                ) {
                    items(searchItemsFound) { movie ->
                        val movieName = movie.title
                        val movieImage = movie.backgroundImage
                        Image(
                            painter = rememberAsyncImagePainter(movieImage),
                            contentDescription = "${movieName} image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(160.dp)
                                .width(200.dp)
                                .clickable {
                                    navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
                                }
                                .padding(8.dp)
                        )
                    }
                }

            }


        }
    }
}
