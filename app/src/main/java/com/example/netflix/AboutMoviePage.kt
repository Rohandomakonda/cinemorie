package com.example.netflix

import android.R
import android.text.Layout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImage

@Composable
fun MovieDetailScreen(
    data: movieDetails
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = data.backgroundImage,
            contentDescription = "${data.title} Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)

        ){
            Surface(
                modifier = Modifier
                    .width(500.dp)
                    .height(300.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(50,41,47).copy(0.95f), // semi-transparent
                tonalElevation = 6.dp, // subtle shadow
                shadowElevation = 8.dp
            ){
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = data.title,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(30.dp,16.dp)
                        )
                        Text(
                            text = data.tags,
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(end = 30.dp)
                                .background(
                                    color = Color.Transparent,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,  // Border thickness
                                    color = Color.White,  // Border color
                                    shape = CircleShape  // Ensure the border follows the circle shape
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)  // Adjust the padding to make sure text is inside the circle
                        )
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        text = data.genres.joinToString(),
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(30.dp,2.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)  // Optional: set icon size
                        )
                        Spacer(modifier = Modifier.width(2.dp))  // Optional spacing between icon and text
                        Text(
                            text = "${data.rating} ${data.year} ${data.ageRating}",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = data.description,
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(30.dp).width(300.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                        ){
                        Button(
                            onClick = {},
                            modifier = Modifier.padding(8.dp).border(border = BorderStroke(2.dp,Color.Transparent),shape = RoundedCornerShape(20))
                                .width(180.dp).border(border = BorderStroke(2.dp,Color.Transparent),shape=RoundedCornerShape(100)),
                            colors = ButtonColors(
                                containerColor = Color(185,40,94),
                                contentColor = Color.White,
                                disabledContentColor = Color.White,
                                disabledContainerColor = Color(185,40,94)
                            )
                        ) {
                            Text("Watch Now")
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.padding(8.dp).border(border = BorderStroke(2.dp,Color.Transparent),shape = RoundedCornerShape(20))
                                .width(200.dp).border(border = BorderStroke(2.dp,Color.White),shape=RoundedCornerShape(100)),
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White,
                                disabledContentColor = Color.White,
                                disabledContainerColor = Color.Transparent
                            )
                        ) {
                            Text("Trailer")
                        }
                    }
                }
            }

            }
        }

    }




data class movieDetails(
    val title: String,
    val description: String,
    val tags: String,
    val rating: Double,
    val year: Int,
    val ageRating: String,
    val backgroundImage: String,
    val castList: List<Pair<String, Painter>>? =null,
    val genres: List<String>
)