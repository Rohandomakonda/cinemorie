package com.example.netflix

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Base64
import android.util.Log
import java.io.File

fun prepareAndCastVideo(context: Context, base64Data: String) {
    // 1. Decode base64
    Log.d("PrepareAndCastVideo", "Base64 Data: ")
    val videoBytes = Base64.decode(base64Data, Base64.DEFAULT)
    Log.d("PrepareAndCastVideo", "Decoded Bytes:")
    // 2. Save to local .mp4 file
    val videoFile = File(context.cacheDir, "video.mp4")
    videoFile.writeBytes(videoBytes)
    Log.d("PrepareAndCastVideo", "Video File Path: ")

    // 3. Start local HTTP server
    val server = LocalVideoServer(videoFile)
    server.start()
   Log.d("PrepareAndCastVideo", "Server Started")
    // 4. Get device IP
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
    Log.d("PrepareAndCastVideo", "Device IP: $ip")
    // 5. Construct local video URL (on port 8060)
    val videoUrl = "http://$ip:8060/"
    Log.d("PrepareAndCastVideo", "Video URL: $videoUrl")
    // 6. Cast the video
    castMedia(context, videoUrl, "My Casted Video")
    Log.d("PrepareAndCastVideo", "Video Cast Complete")
}