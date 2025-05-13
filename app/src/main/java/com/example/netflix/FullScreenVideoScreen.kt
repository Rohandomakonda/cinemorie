package com.example.netflix

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
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
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun FullScreenVideoScreen(navController: NavHostController) {
    val context = LocalContext.current
    val showMinimizeIcon = remember { mutableStateOf(true) }

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

    LaunchedEffect(Unit) {
        val dataSourceFactory = DefaultDataSourceFactory(context, "user-agent")
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUri))

        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()
    }

    var playerView: PlayerView? = null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(factory = {
            PlayerView(context).apply {
                useController = true
                this.player = player
                setShutterBackgroundColor(android.graphics.Color.BLACK)

                // Capture reference
                playerView = this

                // Sync minimize icon with controller visibility
                setControllerVisibilityListener { visibility ->
                    showMinimizeIcon.value = visibility == View.VISIBLE
                }
            }
        }, modifier = Modifier.fillMaxSize())

        if (showMinimizeIcon.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart // Align the icon at the top-left corner
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(16.dp) // Add padding for spacing from the edges
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Minimize",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
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
