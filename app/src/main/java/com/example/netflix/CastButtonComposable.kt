package com.example.netflix
import android.app.Activity
import android.util.Log

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.mediarouter.app.MediaRouteChooserDialog
import androidx.mediarouter.media.MediaRouteSelector
import com.google.android.gms.cast.CastMediaControlIntent

@Composable
fun CustomCastButton() {
    val context = LocalContext.current

    // Create a Cast device selector for default Cast receivers
    val selector = MediaRouteSelector.Builder()
        .addControlCategory(CastMediaControlIntent.categoryForCast("CC1AD845"))
        .build()


    IconButton(onClick = {
        Log.d("CustomCastButton", "Clicked - showing Cast dialog")

        val activity = context as? Activity
        if (activity == null) {
            Log.e("CustomCastButton", "Context is not an Activity, can't show Cast dialog")
            return@IconButton
        }

        // Create and show the Cast device chooser dialog manually
        val dialog = MediaRouteChooserDialog(activity)
        dialog.routeSelector = selector
        dialog.show()

    }) {
        Icon(
            imageVector = Icons.Default.Cast,
            contentDescription = "Cast Button",
            tint = Color.White
        )
    }
}