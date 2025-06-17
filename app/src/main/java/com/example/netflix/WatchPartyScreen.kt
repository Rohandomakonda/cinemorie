package com.example.netflix

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun WatchPartyScreen(
    navController: NavController,
    partyCode: String
) {
    val context = LocalContext.current
    val viewModel: WatchPartyViewModel = viewModel(
        factory = WatchPartyViewModel.Factory(partyCode = partyCode, context = context)
    )
    val videoUrl = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("Video")

    if (videoUrl == null) {
        Text("Video URL missing", color = Color.Red)
        return
    }
    val decodedBytes = Base64.decode(videoUrl)
    val tempFile = File.createTempFile("video", ".mp4", context.cacheDir)
    tempFile.writeBytes(decodedBytes)
    val uri = tempFile.toUri()


    val party = viewModel.party
    val isHost = viewModel.isHost
    val isConnected = viewModel.isConnected
    val participants = viewModel.participants

    val isPlayingState = remember { mutableStateOf(false) }
    val isPlaying by isPlayingState

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // WebSocket events
    LaunchedEffect(Unit) {
        viewModel.connectToParty()
        viewModel.watchEvents.collect { event ->
            when (event.action) {
                "play" -> {
                    exoPlayer.seekTo((event.timestamp * 1000).toLong())
                    exoPlayer.play()
                }
                "pause" -> {
                    exoPlayer.seekTo((event.timestamp * 1000).toLong())
                    exoPlayer.pause()
                }
                "seek" -> {
                    exoPlayer.seekTo((event.timestamp * 1000).toLong())
                }
            }
        }
    }

    // Player Listener
    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                isPlayingState.value = isPlayingNow
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
            viewModel.disconnect()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(20, 20, 20),
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Watch Party",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Code: $partyCode",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isConnected) Icons.Default.Circle else Icons.Default.Close,
                        contentDescription = "Connection Status",
                        tint = if (isConnected) Color.Green else Color.Red,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${participants.size} watching",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Player View
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        ) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Controls
        if (isHost) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(30, 30, 30),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val currentPos = exoPlayer.currentPosition / 1000.0
                            if (exoPlayer.isPlaying) {
                                exoPlayer.pause()
                                viewModel.sendWatchEvent("pause", currentPos)
                            } else {
                                exoPlayer.play()
                                viewModel.sendWatchEvent("play", currentPos)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val newPos = (exoPlayer.currentPosition - 10000).coerceAtLeast(0)
                            exoPlayer.seekTo(newPos)
                            viewModel.sendWatchEvent("seek", newPos / 1000.0)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "Rewind 10 seconds",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            val newPos = exoPlayer.currentPosition + 10000
                            exoPlayer.seekTo(newPos)
                            viewModel.sendWatchEvent("seek", newPos / 1000.0)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Forward 10 seconds",
                            tint = Color.White
                        )
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(30, 30, 30),
                tonalElevation = 8.dp
            ) {
                Text(
                    text = "Only the host can control playback",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Participants
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(25, 25, 25),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Participants (${participants.size})",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                participants.forEach { participantId ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "User $participantId${if (party?.hostId == participantId) " (Host)" else ""}",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
