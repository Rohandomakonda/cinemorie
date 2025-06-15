package com.example.netflix

import android.content.Context
import android.util.Log
import com.google.android.gms.cast.framework.CastContext

fun stopCasting(context: Context) {
    try {
        val castContext = CastContext.getSharedInstance(context)
        val session = castContext.sessionManager.currentCastSession
        session?.remoteMediaClient?.stop()
        castContext.sessionManager.endCurrentSession(true)
        Log.d("Cast", "Stopped casting and ended session.")
    } catch (e: Exception) {
        Log.e("Cast", "Error while stopping cast session: ${e.message}")
    }
}
