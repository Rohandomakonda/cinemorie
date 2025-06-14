package com.example.netflix

import android.content.Context
import android.util.Log
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient

fun castMedia(context: Context, videoUrl: String, title: String) {
    Log.d("CastMedia", "Attempting to cast media with URL: $videoUrl")
    val session: CastSession? = CastContext.getSharedInstance(context).sessionManager.currentCastSession
    Log.d("CastMedia", "Current Cast Session: $session")
    val remoteMediaClient: RemoteMediaClient? = session?.remoteMediaClient
    Log.d("CastMedia", "Remote Media Client: $remoteMediaClient")

    val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
    metadata.putString(MediaMetadata.KEY_TITLE, title)
    Log.d("CastMedia", "Metadata: $metadata")

    val mediaInfo = MediaInfo.Builder(videoUrl)
        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
        .setContentType("video/mp4")
        .setMetadata(metadata)
        .build()
    Log.d("CastMedia", "Media Info: $mediaInfo")
    remoteMediaClient?.load(mediaInfo, MediaLoadOptions.Builder().build())
    Log.d("CastMedia", "Media Loaded")

}