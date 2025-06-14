package com.example.netflix

import com.google.android.gms.common.api.Response
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream

class LocalVideoServer(private val videoFile: File) : NanoHTTPD(8060) {
    override fun serve(session: IHTTPSession): Response {
        val fis = FileInputStream(videoFile)
        return newFixedLengthResponse(
            Response.Status.OK,
            "video/mp4",
            fis,
            videoFile.length()
        )
    }
}