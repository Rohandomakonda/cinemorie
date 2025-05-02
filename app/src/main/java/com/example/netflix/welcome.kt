package com.example.netflix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter


@Preview(showBackground = true)
@Composable
fun WelcomeScreen() {
    val lightPurple = Color(0xFF9F80C2)
    val pinkPurple = Color(0xFFEC6BF2)
    val gradient = Brush.linearGradient(colors = listOf(lightPurple, pinkPurple))

    val violetGradient = listOf(Color(0xFF4C1D95), Color(0xFF1E1B4B))
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = rememberAsyncImagePainter("https://st.depositphotos.com/35570512/60698/v/450/depositphotos_606980010-stock-illustration-dark-purple-background-abstract-geometric.jpg"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Optional dark overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Content over image
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Cinémoire",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 45.sp,
                    brush = gradient
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Ultimate Destination for Movies, Shows, and Stories That Matter",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    brush = gradient
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                        containerColor = Color(63, 51, 101),
                contentColor = Color.White
            ),
            ) {
                Text("Login")
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(50.dp)
                    .border(border = BorderStroke(2.dp, Color(63, 51, 101)), shape = RoundedCornerShape(60)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(60)
            ) {
                Text("Sign Up")
            }
        }
    }
}
