package com.example.netflix

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.netflix.UserPreferences.AuthPreferences
import com.example.netflix.retrofit.watchlistApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

    // State for managing dropdown and selected season
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedSeasonIndex by remember { mutableStateOf(0) }

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

    val context = LocalContext.current
    val authPreferences = AuthPreferences(context)
    val profile by authPreferences.profileData.collectAsState(initial = null)
    val profileId= profile?.id
    val auth by authPreferences.authData.collectAsState(initial = null)
    val at=auth?.accessToken

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

                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        if (at != null && profileId != null) {
                                            val response = data?.let {
                                                watchlistApi.addserieswatchlist(
                                                    token = at,
                                                    id = null,
                                                    userid = profileId,
                                                    seriesid = it.seriesId.toLong()
                                                )
                                            }

                                            if (response != null) {
                                                if (response.isSuccessful) {
                                                    Toast.makeText(context, "Successfully added into watchlist", Toast.LENGTH_SHORT).show()
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
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Text("+ Watchlist")
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Safe seasons handling with dropdown menu
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
                                // Episodes section header and dropdown
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Episodes",
                                        fontSize = 18.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Season dropdown menu
                                    Box(
                                        modifier = Modifier.width(150.dp)
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { isDropdownExpanded = true },
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White.copy(alpha = 0.15f)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = if (seasons.isNotEmpty() && selectedSeasonIndex < seasons.size) {
                                                        "Season ${seasons[selectedSeasonIndex].seasonNumber}"
                                                    } else {
                                                        "Select Season"
                                                    },
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.White
                                                )

                                                Icon(
                                                    imageVector = if (isDropdownExpanded)
                                                        Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                                    contentDescription = "Season dropdown",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }

                                        // Dropdown menu
                                        DropdownMenu(
                                            expanded = isDropdownExpanded,
                                            onDismissRequest = { isDropdownExpanded = false },
                                            modifier = Modifier.background(
                                                Color(40, 40, 40),
                                                RoundedCornerShape(8.dp)
                                            )
                                        ) {
                                            seasons.forEachIndexed { index, season ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = "Season ${season.seasonNumber}",
                                                            color = Color.White,
                                                            fontSize = 14.sp
                                                        )
                                                    },
                                                    onClick = {
                                                        selectedSeasonIndex = index
                                                        isDropdownExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Display episodes for selected season
                                if (seasons.isNotEmpty() && selectedSeasonIndex < seasons.size) {
                                    val selectedSeason = seasons[selectedSeasonIndex]

                                    Column {
                                        selectedSeason.episodes.forEach { episode ->
                                            val painter = rememberAsyncImagePainter(episode.thumbnailUrl)

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
                                                            .height(120.dp)
                                                            .width(120.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(Color.Gray)
                                                            .border(
                                                                1.dp,
                                                                Color.White.copy(alpha = 0.3f),
                                                                RoundedCornerShape(12.dp)
                                                            ),
                                                        contentScale = ContentScale.Crop
                                                    )

                                                    Box(
                                                        modifier = Modifier.background(
                                                            Color.Black.copy(alpha = 0.6f),
                                                            CircleShape
                                                        )
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.PlayArrow,
                                                            contentDescription = "Play episode",
                                                            modifier = Modifier
                                                                .size(40.dp)
                                                                .padding(8.dp),
                                                            tint = Color.White
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.width(16.dp))

                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(
                                                        text = "Episode ${episode.episodeNumber}: ${episode.title}",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Color.White
                                                    )

                                                    Spacer(modifier = Modifier.height(4.dp))

                                                    Text(
                                                        text = "Released: ${episode.releaseDate}",
                                                        fontSize = 12.sp,
                                                        color = Color.LightGray
                                                    )

                                                    Text(
                                                        text = "Duration: ${episode.duration} min",
                                                        fontSize = 12.sp,
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
}