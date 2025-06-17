package com.example.netflix

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netflix.UserPreferences.AuthPreferences
import com.example.netflix.dtos.LoginRequest
import com.example.netflix.retrofit.authApi
import com.example.netflix.retrofit.watchlistApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MovieDetailScreen(
    navController: NavController
) {
    val movie = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("movie")
    }
    val context = LocalContext.current
    val viewModel: MovieDetailViewModel = viewModel(
        factory = MovieDetailViewModel.Factory(movie,context)
    )

    val data = viewModel.getMovie()
    val showDialog = viewModel.showDialog
    val inviteCode = viewModel.inviteCode
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val createdPartyCode = viewModel.createdPartyCode

    val authPreferences = AuthPreferences(context)
    val profile by authPreferences.profileData.collectAsState(initial = null)
    val profileId= profile?.id
    val auth by authPreferences.authData.collectAsState(initial = null)
    val at=auth?.accessToken

    Box(modifier = Modifier.fillMaxSize()) {
        if (data != null) {
            AsyncImage(
                model = data?.thumbnailUrl,
                contentDescription = "${data?.title} Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }

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
                color = Color(50, 41, 47).copy(alpha = 0.95f),
                tonalElevation = 6.dp,
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
                                text = data?.title ?: "Unknown Title",
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
                                    .background(Color.Transparent, shape = CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = data?.genre ?: "",
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 30.dp)
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
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${data?.rating ?: ""} • ${data?.releaseDate ?: ""}",
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = data?.description ?: "",
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 30.dp, top = 8.dp)
                                .width(300.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    data?.let {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "movie",
                                            it
                                        )
                                        navController.navigate(Screen.OtherPage.FullVideoScreen.bRoute)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(185, 40, 94),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Watch Now")
                            }

                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        try {
                                            if (at != null && profileId != null) {
                                                val response = movie?.let {
                                                    watchlistApi.addMovieToWatchlist(
                                                        token = at,
                                                        id = null,
                                                        userid = profileId,
                                                        movieid = it.id.toLong()
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
                                }
                                ,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(2.dp, Color.White)
                            ) {
                                Text("+ Watchlist")
                            }

                            Button(
                                onClick = { viewModel.toggleDialog(true) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(2.dp, Color.White)
                            ) {
                                Text("🎉 Party")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { viewModel.toggleDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(30, 30, 30),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🎉 Watch Party",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (createdPartyCode.isNotEmpty()) {
                        // Show created party code
                        Surface(
                            color = Color(40, 40, 40),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Party Created!",
                                    color = Color.Green,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Invite Code:", color = Color.Gray, fontSize = 14.sp)
                                Text(
                                    createdPartyCode,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        data?.let {
    navController.currentBackStackEntry?.savedStateHandle?.set("Video", it.videoData)
}
Log.d("Stomp", data?.videoData?:"null")
val route = Screen.OtherPage.WatchParty.bRoute.replace("{partyCode}", createdPartyCode)
navController.navigate(route)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                                ) {
                                    Text("Start Watching")
                                }
                            }
                        }
                    } else {
                        // Show create/join options
                        Button(
                            onClick = { viewModel.createParty() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(2.dp, Color.White),
                                    shape = RoundedCornerShape(100)
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Create Party")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("OR", color = Color.LightGray)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = inviteCode,
                            onValueChange = { viewModel.updateInviteCode(it) },
                            label = { Text("Invite Code") },
                            singleLine = true,
                            enabled = !isLoading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Magenta,
                                unfocusedBorderColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.joinParty()
                                // Navigate to party screen after successful join
                                if (errorMessage.contains("Successfully joined")) {
                                    navController.navigate("watch_party/$inviteCode")
                                    viewModel.toggleDialog(false)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                            enabled = !isLoading && inviteCode.isNotBlank()
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Join Party")
                            }
                        }
                    }

                        // Error/Success Message
                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = errorMessage,
                                color = if (errorMessage.contains("Success") || errorMessage.contains(
                                        "created"
                                    )
                                ) Color.Green else Color.Red,
                                fontSize = 12.sp
                            )
                        }


                        if (errorMessage.contains("Successfully joined")) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    navController.navigate("watch_party/${inviteCode.trim()}")
                                    viewModel.toggleDialog(false)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                            ) {
                                Text("Enter Party")
                            }
                        }
                    }
                }
            }
        }
    }
