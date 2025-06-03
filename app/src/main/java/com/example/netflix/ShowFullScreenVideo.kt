package com.example.netflix

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowFullScreenVideo(
    navController: NavHostController
) {
    val context = LocalContext.current
    val showControls = remember { mutableStateOf(true) }

    val initialEpisode = navController.previousBackStackEntry?.savedStateHandle?.get<Episode>("episode")
    val series = navController.previousBackStackEntry?.savedStateHandle?.get<Series>("series")

    if (initialEpisode == null || series == null) {
        Log.d("ShowFullScreenVideo", "Episode or Series data not received!")
        return
    }

    // State holding current episode being played
    val currentEpisode = remember { mutableStateOf(initialEpisode) }

    // Function to get next episode of the series relative to current episode
    fun getNextEpisode(series: Series, currentEpisode: Episode): Episode? {
        // Find the season that contains the current episode by episodeId
        val currentSeason = series.seasons.find { season ->
            season.episodes.any { it.episodeId == currentEpisode.episodeId }
        } ?: return null

        val sortedEpisodes = currentSeason.episodes.sortedBy { it.episodeNumber }

        // Find current episode index using episodeNumber match
        val currentIndex = sortedEpisodes.indexOfFirst { it.episodeNumber == currentEpisode.episodeNumber }
        if (currentIndex == -1) return null

        // Check if next episode exists in current season
        if (currentIndex + 1 < sortedEpisodes.size) {
            return sortedEpisodes[currentIndex + 1]
        }

        // Else, check the next season
        val sortedSeasons = series.seasons.sortedBy { it.seasonNumber }
        val currentSeasonIndex = sortedSeasons.indexOfFirst { it.seasonid == currentSeason.seasonid }
        if (currentSeasonIndex == -1) return null

        if (currentSeasonIndex + 1 < sortedSeasons.size) {
            return sortedSeasons[currentSeasonIndex + 1].episodes.minByOrNull { it.episodeNumber }
        }

        // No next episode found
        return null
    }






    val nextEpisode = getNextEpisode(series, currentEpisode.value)

    // Create player instance and release when composable disposes
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Manage video file and URI for current episode, update when episode changes
    val videoFileAndUri = remember(currentEpisode.value) {
        // Clean old temp files if needed (optional)
        // Decode base64 and write to temp file
        val decodedVideoData = Base64.decode(currentEpisode.value.fileUrl, Base64.DEFAULT)
        val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
        FileOutputStream(tempFile).use { it.write(decodedVideoData) }
        Uri.fromFile(tempFile) to tempFile
    }

    val videoUri = videoFileAndUri.first
    val videoFile = videoFileAndUri.second

    // Playback control states
    val isPlaying = remember { mutableStateOf(true) }
    val position = remember { mutableStateOf(0L) }
    val duration = remember { mutableStateOf(1L) }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    // Update player media source and play on episode change
    LaunchedEffect(currentEpisode.value) {
        val dataSourceFactory = DefaultDataSourceFactory(context, "user-agent")
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUri))

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    // Listener to update playing state
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                isPlaying.value = isPlayingNow
            }
        })

        while (true) {
            position.value = exoPlayer.currentPosition
            duration.value = exoPlayer.duration.coerceAtLeast(1L)
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
                player = exoPlayer
                setShutterBackgroundColor(android.graphics.Color.BLACK)
            }
        }, modifier = Modifier.fillMaxSize())

        if (showControls.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(screenHeightDp / 2))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                            isPlaying.value = exoPlayer.isPlaying
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

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Slider(
                        value = position.value.toFloat(),
                        onValueChange = { exoPlayer.seekTo(it.toLong()) },
                        valueRange = 0f..duration.value.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Transparent,
                            activeTrackColor = Color.Red,
                            inactiveTrackColor = Color.Gray
                        ),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color.White, shape = CircleShape)
                            )
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${formatTime(position.value)} / ${formatTime(duration.value)}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(2.dp)
                            )

                            Row {
                                IconButton(onClick = {
                                    exoPlayer.seekTo((exoPlayer.currentPosition - 10000).coerceAtLeast(0))
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_replay_10_24),
                                        contentDescription = "Rewind 10s",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                IconButton(onClick = {
                                    exoPlayer.seekTo((exoPlayer.currentPosition + 10000).coerceAtMost(exoPlayer.duration))
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_forward_10_24),
                                        contentDescription = "Forward 10s",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_fullscreen_exit_24),
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            if (nextEpisode != null) {
                                Button(onClick = {
                                    // Update currentEpisode state — this triggers recomposition and media reload
                                    currentEpisode.value = nextEpisode
                                }) {
                                    Text("Next Episode")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
