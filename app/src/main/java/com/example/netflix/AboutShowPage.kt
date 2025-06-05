package com.example.netflix

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter


@Composable
fun ShowDetailScreen(
    navController: NavController
) {
    val data = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<Series>("series")
    }?.let { series ->
        series.copy(
            seasons = ArrayList(
                series.seasons
                    .sortedBy { it.seasonNumber }
                    .map { season ->
                        season.copy(
                            episodes = ArrayList(season.episodes.sortedBy { it.episodeNumber })
                        )
                    }
            )
        )
    }
    if (data != null) {
        for(episodes in data?.seasons?.get(0)?.episodes!!){
            Log.d("Episodes", episodes.episodeNumber.toString())
        }
    }


    if (data != null) {
        Log.d("ShowInfo", "Received show: $data")
    } else {
        Log.d("ShowInfo", "No show received!")
    }



    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = data?.thumbnailUrl,
            contentDescription = "${data?.title} Background",
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
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .width(500.dp)
                    .height(300.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(50, 41, 47).copy(0.95f), // semi-transparent
                tonalElevation = 6.dp, // subtle shadow
                shadowElevation = 8.dp
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = data?.title ?: "No title",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(30.dp, 16.dp)
                            )
                            Text(
                                text = "A",
                                fontSize = 15.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(end = 30.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(0.dp))

                        Text(
                            text = data?.genres.toString(),
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier.padding(30.dp, 2.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = " ${data?.releaseDate ?: "Unknown"}",
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = data?.description ?: "No description available",
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 30.dp).width(300.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Safe seasons handling
                        val seasons = data?.seasons

                        if (seasons.isNullOrEmpty()) {
                            // Show placeholder if no seasons/episodes available
                            Text(
                                text = "No episodes available.",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(30.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(24.dp))
                            Column(modifier = Modifier.padding(30.dp)) {
                                Text(
                                    text = "Episodes",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )

                                seasons.forEach { season ->
                                    Text(
                                        text = "Season ${season.seasonNumber}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    season.episodes.forEach { episode ->
                                        val painter =
                                            rememberAsyncImagePainter(episode.thumbnailUrl)

                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier.clickable {
                                                    navController.currentBackStackEntry?.savedStateHandle?.set("episode", episode)
                                                    navController.currentBackStackEntry?.savedStateHandle?.set("series", data)
                                                    navController.navigate(Screen.OtherPage.ShowFullScreenVideo.bRoute)
                                                },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painter,
                                                    contentDescription = episode.title,
                                                    modifier = Modifier
                                                        .height(150.dp)
                                                        .width(150.dp)
                                                        .clip(RoundedCornerShape(16.dp))
                                                        .background(Color.Gray)
                                                        .border(
                                                            2.dp,
                                                            Color.White,
                                                            RoundedCornerShape(16.dp)
                                                        ),
                                                    contentScale = ContentScale.Crop
                                                )

                                                Box(
                                                    modifier = Modifier.background(
                                                        Color.Gray.copy(
                                                            0.6f
                                                        ), CircleShape
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.PlayArrow,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .clip(CircleShape)
                                                            .border(
                                                                BorderStroke(2.dp, Color.Black),
                                                                CircleShape
                                                            )
                                                            .size(50.dp),
                                                        tint = Color.Black
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(30.dp))

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = episode.title,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White
                                                )


                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Released: ${episode.releaseDate}",
                                                    fontSize = 13.sp,
                                                    color = Color.LightGray
                                                )

                                                Text(
                                                    text = "Duration: ${episode.duration} min",
                                                    fontSize = 13.sp,
                                                    color = Color.LightGray
                                                )
                                                }
                                        }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
//                            Spacer(modifier = Modifier.height(8.dp))
//                        if (!data.morelikethis.isNullOrEmpty()) {
//                            Column(modifier = Modifier.padding(30.dp, 8.dp)) {
//                                Text(
//                                    text = "More Like this: ",
//                                    fontSize = 15.sp,
//                                    color = Color.White,
//                                    modifier = Modifier.padding()
//                                )
//                                Spacer(modifier = Modifier.height(16.dp))
//                                LazyRow() {
//                                    items(data.morelikethis) {(name,image)->
//                                        val imagePainter = rememberAsyncImagePainter(image)
//                                        Column(modifier = Modifier.padding(end=8.dp).width(105.dp)){
//                                            Image(
//                                                painter = imagePainter,
//                                                contentDescription = "${name} picture",
//                                                modifier = Modifier
//                                                    .size(100.dp)
//                                                    .clip(RoundedCornerShape(20))
//                                                    .background(Color.Gray, RoundedCornerShape(20))
//                                                    .border(2.dp, Color.White, RoundedCornerShape(20)),
//                                                contentScale = ContentScale.Crop
//                                            )
//                                            Spacer(modifier = Modifier.height(6.dp))
//                                            Text(
//                                                text = name,
//                                                fontSize = 15.sp,
//                                                color = Color.White
//                                            )
//                                        }
//                                    }
//                                }
//                            }







