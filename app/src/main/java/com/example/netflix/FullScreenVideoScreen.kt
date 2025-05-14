package com.example.netflix

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenVideoScreen(navController: NavHostController) {
    val context = LocalContext.current
    val showControls = remember { mutableStateOf(true) }

    val movie = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("movie")
    }

    if (movie == null) {
        Log.d("MovieInfo", "No movie received!")
        return
    }

    val decodedVideoData = Base64.decode(movie.videoData, Base64.DEFAULT)

    val videoFile = remember {
        val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
        FileOutputStream(tempFile).use { it.write(decodedVideoData) }
        tempFile
    }
    val videoUri = remember { Uri.fromFile(videoFile) }

    val player = remember { ExoPlayer.Builder(context).build() }
    val isPlaying = remember { mutableStateOf(true) }
    val position = remember { mutableStateOf(0L) }
    val duration = remember { mutableStateOf(1L) }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    LaunchedEffect(Unit) {
        val dataSourceFactory = DefaultDataSourceFactory(context, "user-agent")
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUri))

        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                isPlaying.value = isPlayingNow
            }
        })

        while (true) {
            position.value = player.currentPosition
            duration.value = player.duration.coerceAtLeast(1L)
            kotlinx.coroutines.delay(500)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls.value = !showControls.value }
    ) {
        AndroidView(factory = {
            PlayerView(context).apply {
                useController = false
                this.player = player
                setShutterBackgroundColor(android.graphics.Color.BLACK)
            }
        }, modifier = Modifier.fillMaxSize())

        if (showControls.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Spacer: Adding empty space at the top
                Spacer(modifier = Modifier.height(screenHeightDp/2)) // Adjust height based on your preference

                // Center: Play/Pause
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (player.isPlaying) player.pause() else player.play()
                            isPlaying.value = player.isPlaying
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0x66000000), shape = MaterialTheme.shapes.medium)
                    ) {
                        Icon(
                            imageVector = if (isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                // Spacer between play button and bottom controls
                Spacer(modifier = Modifier.weight(1f))

                // Bottom: Seek bar + time + controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    // Duration bar (seek bar)
                    androidx.compose.material3.Slider(
                        value = position.value.toFloat(),
                        onValueChange = { player.seekTo(it.toLong()) },
                        valueRange = 0f..duration.value.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Transparent, // Set to transparent to avoid default drawing
                            activeTrackColor = Color.Red,
                            inactiveTrackColor = Color.Gray
                        ),
                        thumb = {
                            // Custom white circular thumb
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape) // You can tweak the size
                                    .background(Color.White, shape = CircleShape)
                            )
                        }
                    )

                    // Bottom row: rewind, forward, time
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 1.dp), // Reduced padding
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            // Time display
                            Text(
                                text = "${formatTime(position.value)} / ${formatTime(duration.value)}", // Displays formatted time
                                color = Color.White, // Text color is set to white
                                style = MaterialTheme.typography.bodySmall, // The style uses a small body text from the MaterialTheme
                                modifier = Modifier.padding(2.dp) // Reduced padding for the Text element
                            )

                            Row {
                                IconButton(onClick = {
                                    player.seekTo((player.currentPosition - 10000).coerceAtLeast(0))
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_replay_10_24),
                                        contentDescription = "Rewind 10s",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp) // Reduced icon size
                                    )
                                }

                                IconButton(onClick = {
                                    player.seekTo((player.currentPosition + 10000).coerceAtMost(player.duration))
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_forward_10_24),
                                        contentDescription = "Forward 10s",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp) // Reduced icon size
                                    )
                                }
                            }
                        }

                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_fullscreen_exit_24),
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp) // Reduced back button size
                            )
                        }
                    }

                }
            }
        }
    }



    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
}

// Format milliseconds to MM:SS or HH:MM:SS
fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0)
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    else
        String.format("%02d:%02d", minutes, seconds)
}