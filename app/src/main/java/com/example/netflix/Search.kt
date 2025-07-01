package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.netflix.UserPreferences.AuthPreferences


fun getClosestMatchesByLevenshtein(search: String, list: List<MediaItem>): List<MediaItem> {
    return list.filter {
        it.title.contains(search, ignoreCase = true)
    }
}


@Composable
fun Search(
    navController: NavController
) {
    val violetGradient = listOf(Color(0xFF4C1D95), Color(0xFF1E1B4B))
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }

    var searchText by remember { mutableStateOf("") }
    var searchItemsFound by remember { mutableStateOf<List<MediaItem>>(emptyList()) }

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

        val allItems: List<MediaItem> = movies.map { MovieItem(it) } + shows.map { SeriesItem(it) }

        // update result list
        searchItemsFound = getClosestMatchesByLevenshtein(searchText, allItems)

        Box(modifier = Modifier.background(gradient(true, darkVioletColors))) {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        searchItemsFound = getClosestMatchesByLevenshtein(it, allItems)
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
                        .padding(top = 40.dp),
                    shape = RoundedCornerShape(100)
                )

                if (searchItemsFound.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Sorry, we don't have that!",
                            fontSize = 25.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Please search for something else.",
                            fontSize = 15.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
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
                        items(searchItemsFound) { item ->
                            Image(
                                painter = rememberAsyncImagePainter(item.thumbnailUrl),
                                contentDescription = "${item.title} image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(160.dp)
                                    .width(200.dp)
                                    .clickable {
                                        when (item) {
                                            is MovieItem -> {
                                                navController.currentBackStackEntry
                                                    ?.savedStateHandle
                                                    ?.set("movie", item.movie)
                                                navController.navigate(Screen.OtherPage.MovieInfo.bRoute)
                                            }

                                            is SeriesItem -> {
                                                navController.currentBackStackEntry
                                                    ?.savedStateHandle
                                                    ?.set("series", item.series)
                                                navController.navigate(Screen.OtherPage.ShowInfo.bRoute)
                                            }
                                        }
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
