package com.example.netflix

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.netflix.dtos.VerificationRequest
import com.example.netflix.retrofit.authApi
import kotlinx.coroutines.launch


@Composable
fun verificationpage(navController: NavController ) {
    val lightPurple = Color(0xFF9F80C2)
    val pinkPurple = Color(0xFFEC6BF2)
    val LightBlue = Color(0xFF03A9F4)
    val email = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<String>("email")
    }
    Log.d("registering", "email: $email")
    val gradient = Brush.linearGradient(
        colors = listOf(lightPurple, pinkPurple)
    )
    val violetGradient = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }
    var otp by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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

        // Foreground content without extra gradient
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add padding if needed
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Verification",
                style = TextStyle(
                    fontWeight = FontWeight.Bold, // Makes the text bold
                    fontSize = 45.sp,
                    brush = gradient  // Light Purple 100))
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "enetr the otp sent to your email",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        brush = gradient
                    ),
                    textAlign = TextAlign.Center
                )
            }
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter the OTP", color = Color.White) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_key_24),
                        contentDescription = "Key Icon",
                        tint = Color.White
                    )

                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().background(Color.Transparent)
            )

            Button(
                onClick =  {
                    coroutineScope.launch {
                        val request = email?.let { VerificationRequest(email = it, otp = otp) }

                        try {
                            val response = request?.let { authApi.verify(it) }

                            if (response != null) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Verification successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.OtherPage.Login.bRoute)
                                } else {
                                    Toast.makeText(context, "Invalid OTP or email", Toast.LENGTH_SHORT).show()
                                    Log.d("verify", "Failed: ${response.code()}")
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                            Log.d("verify", "Error: ${e.localizedMessage}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E3C8D))
            ) {
                Text("Verify", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "resend OTP",
                color = LightBlue,
                modifier = Modifier.clickable { }
            )

        }
    }
}