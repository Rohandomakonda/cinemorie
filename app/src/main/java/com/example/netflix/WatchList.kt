package com.example.netflix

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun WatchList(navController: NavController) {
    val movies = listOf(
        movieDetails(
            title = "Action",
            description = "Explosive stunts and thrilling scenes.",
            tags = "Action",
            rating = 7.8,
            year = 2019,
            ageRating = "13+",
            backgroundImage = "https://www.tallengestore.com/cdn/shop/products/Fast_Furious_Presents_Hobbs_Shaw_-_Dwayne_Rock_Johnson_-_Jason_Statham_Idris_Alba_-_Tallenge_Hollywood_Action_Movie_Poster_dc2cfde0-101a-4bc4-a5c3-b469bc0c2fa8.jpg?v=1582543424",
            genres = listOf("Action", "Adventure")
        ),
        movieDetails(
            title = "Romance",
            description = "A heartfelt story of love and longing.",
            tags = "Romance",
            rating = 8.2,
            year = 2021,
            ageRating = "13+",
            backgroundImage = "https://m.media-amazon.com/images/M/MV5BZjE0ZjgzMzYtMTAxYi00NGMzLThmZDktNzFlMzA2MWRmYWQ0XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            genres = listOf("Romance", "Drama")
        ),
        movieDetails(
            title = "Sci-fi",
            description = "Parallel worlds and existential mysteries.",
            tags = "Sci-fi",
            rating = 7.9,
            year = 2011,
            ageRating = "13+",
            backgroundImage = "https://www.indiewire.com/wp-content/uploads/2017/09/another-earth-2011.jpg?w=674",
            genres = listOf("Sci-fi", "Mystery")
        ),
        movieDetails(
            title = "Horror",
            description = "A chilling tale to keep you up at night.",
            tags = "Horror",
            rating = 6.5,
            year = 2016,
            ageRating = "16+",
            backgroundImage = "https://99designs-blog.imgix.net/blog/wp-content/uploads/2016/10/Dont-Speak.jpg?auto=format&q=60&fit=max&w=930",
            genres = listOf("Horror", "Thriller")
        ),
        movieDetails(
            title = "Comedy",
            description = "Laugh-out-loud moments guaranteed.",
            tags = "Comedy",
            rating = 7.2,
            year = 2009,
            ageRating = "13+",
            backgroundImage = "https://www.discountdisplays.co.uk/our-blog/wp-content/uploads/the-hangover-movie-poster.jpg",
            genres = listOf("Comedy")
        ),
        movieDetails(
            title = "Thriller",
            description = "Dark twists and intense suspense.",
            tags = "Thriller",
            rating = 8.0,
            year = 2014,
            ageRating = "16+",
            backgroundImage = "https://i.pinimg.com/736x/71/3c/bd/713cbd0590734a208fe5e8796715a6cf.jpg",
            genres = listOf("Thriller", "Crime")
        )
    )


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(movies){
            watchlistcard(it)

        }
    }


}
@Composable
fun watchlistcard(movieItem: movieDetails) {
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
                modifier = Modifier.clickable { },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(movieItem.backgroundImage),
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
                    text = "${movieItem.year} • ${movieItem.rating} ⭐ • ${movieItem.genres?.joinToString()}",
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