package com.example.netflix
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory

@Composable
fun CastButtonComposable() {
    val context = LocalContext.current
    val themedContext = ContextThemeWrapper(context, R.style.Theme_Netflix)
Log.d("CastButtonComposable", "CastButtonComposable called")
    AndroidView(factory = {
        MediaRouteButton(themedContext)
    }, modifier = Modifier.size(40.dp))
}

