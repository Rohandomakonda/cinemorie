package com.example.netflix

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun GenrePage() {
    val genres = listOf(
        ContentItem(url = "https://www.tallengestore.com/cdn/shop/products/Fast_Furious_Presents_Hobbs_Shaw_-_Dwayne_Rock_Johnson_-_Jason_Statham_Idris_Alba_-_Tallenge_Hollywood_Action_Movie_Poster_dc2cfde0-101a-4bc4-a5c3-b469bc0c2fa8.jpg?v=1582543424", title = "Action"),
        ContentItem(url = "https://m.media-amazon.com/images/M/MV5BZjE0ZjgzMzYtMTAxYi00NGMzLThmZDktNzFlMzA2MWRmYWQ0XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg", title = "Romance"),
        ContentItem(url = "https://www.indiewire.com/wp-content/uploads/2017/09/another-earth-2011.jpg?w=674", title = "Sci-fi"),
        ContentItem(url = "https://99designs-blog.imgix.net/blog/wp-content/uploads/2016/10/Dont-Speak.jpg?auto=format&q=60&fit=max&w=930", title = "Horror"),
        ContentItem(url = "https://www.discountdisplays.co.uk/our-blog/wp-content/uploads/the-hangover-movie-poster.jpg", title = "Comedy"),
        ContentItem(url = "https://i.pinimg.com/736x/71/3c/bd/713cbd0590734a208fe5e8796715a6cf.jpg", title = "Thriller")
    )


    val violetColors = listOf(

        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),  // Dark Violet 500// Violet 700

    )
    val darkVioletColors = violetColors.map { darken(it, 0.3f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient(isVertical = true, colors = darkVioletColors)) // Full screen gradient
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(genres) { genre ->
               Card3(item = genre)
            }
        }
    }
}



@Composable
fun Card3(item: ContentItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.url),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)) // Rounded edges
        )
    }
}