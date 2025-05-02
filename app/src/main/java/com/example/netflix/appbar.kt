package com.example.netflix



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun darken(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

@Composable
fun appbar() {

        val violetColors = listOf(

            Color(0xFF4C1D95),
            Color(0xFF1E1B4B),  // Dark Violet 500// Violet 700

    )
    val lightPurple = Color(0xFF9F80C2)  // #9F80C2
    val pinkPurple = Color(0xFFEC6BF2)  // #EC6BF2

    val gradient = Brush.linearGradient(
        colors = listOf(lightPurple, pinkPurple)
    )

    val darkVioletColors = violetColors.map { darken(it, 0.3f) }



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradient(false, darkVioletColors))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Cinémoire",
            color = Color.White,
            style = TextStyle(
                fontWeight = FontWeight.Bold, // Makes the text bold
                fontSize = 24.sp,
               brush = gradient  // Light Purple 100))
            ),

        )

        IconButton(onClick = { /* Handle the click */ }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.White
            )
        }
    }
}

@Composable
fun gradient(
    isVertical: Boolean,
    colors: List<Color>
): Brush {
    val endOffset = if (isVertical) {
        Offset(0f, 1000f) // Large vertical gradient
    } else {
        Offset(1000f, 0f) // Large horizontal gradient
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffset
    )
}

@Preview(showBackground = true)
@Composable
fun appbarPreview(){
    appbar()
}