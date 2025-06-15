package com.example.netflix
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Row

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.mediarouter.app.MediaRouteChooserDialog
import androidx.mediarouter.media.MediaRouteSelector
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
@Composable
fun CustomCastButton(movieBase64: String) {
    val context = LocalContext.current
    val activity = context as? Activity

    val selector = remember {
        MediaRouteSelector.Builder()
            .addControlCategory(CastMediaControlIntent.categoryForCast("CC1AD845"))
            .build()
    }

    var didCast by remember { mutableStateOf(false) }
    var isCasting by remember { mutableStateOf(false) }
    var session by remember { mutableStateOf<CastSession?>(null) }

    val castContext = remember { CastContext.getSharedInstance(context) }
    val sessionManager = castContext.sessionManager

    DisposableEffect(Unit) {
        val listener = object : SessionManagerListener<CastSession> {
            override fun onSessionStarted(s: CastSession, sessionId: String) {
                session = s
                isCasting = true
                if (!didCast) {
                    Log.d("CustomCastButton", "Session started, casting movie")
                    prepareAndCastVideo(context, movieBase64)
                    didCast = true
                }
            }

            override fun onSessionResumed(s: CastSession, wasSuspended: Boolean) {
                session = s
                isCasting = true
                if (!didCast) {
                    Log.d("CustomCastButton", "Session resumed, casting movie")
                    prepareAndCastVideo(context, movieBase64)
                    didCast = true
                }
            }

            override fun onSessionEnded(s: CastSession, error: Int) {
                session = null
                isCasting = false
                didCast = false
                Log.d("CustomCastButton", "Session ended")
            }

            override fun onSessionSuspended(session: CastSession, reason: Int) {}
            override fun onSessionResumeFailed(session: CastSession, error: Int) {}
            override fun onSessionResuming(session: CastSession, sessionId: String) {}
            override fun onSessionStartFailed(session: CastSession, error: Int) {}
            override fun onSessionStarting(session: CastSession) {}
            override fun onSessionEnding(session: CastSession) {}
        }

        sessionManager.addSessionManagerListener(listener, CastSession::class.java)
        session = sessionManager.currentCastSession
        if (session?.isConnected == true) {
            isCasting = true
        }

        onDispose {
            sessionManager.removeSessionManagerListener(listener, CastSession::class.java)
        }
    }

    Row {
        if (!isCasting) {
            IconButton(onClick = {
                if (activity == null) {
                    Log.e("CustomCastButton", "Context is not Activity")
                    return@IconButton
                }
                val dialog = MediaRouteChooserDialog(activity)
                dialog.routeSelector = selector
                dialog.show()
            }) {
                Icon(
                    imageVector = Icons.Default.Cast,
                    contentDescription = "Cast",
                    tint = Color.White
                )
            }
        } else {
            IconButton(onClick = {
                stopCasting(context)
                session = null
                isCasting = false
                didCast = false
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Stop Casting",
                    tint = Color.Red
                )
            }
        }
    }
}
