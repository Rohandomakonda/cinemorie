package com.example.netflix

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.netflix.dtos.RegisterRequest
import kotlinx.coroutines.launch
import com.example.netflix.retrofit.authApi


@Composable
fun RegisterPage(navController: NavController) {
    val lightPurple = Color(0xFF9F80C2)
    val pinkPurple = Color(0xFFEC6BF2)

    val gradient = Brush.linearGradient(
        colors = listOf(lightPurple, pinkPurple)
    )
    val violetGradient = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetGradient.map { darken(it, 0.5f) }
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                "Register",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    brush = gradient
                )
            )
            Text(
                text = "Create your account",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    brush = gradient
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Username", color = Color.White) },
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Person,
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = Color.White) },
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Email,
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "lock Icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = Color.White) },
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "lock Icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By registering, you agree to our Terms and Privacy Policy",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (confirmPassword == password) {
                        coroutineScope.launch {
                            loading = true
                            val request = RegisterRequest(name, email, password)
                            Log.d("registering", " typed info $request")
                            try {
                                val response = authApi.register(request)
                                Log.d("registering", " response $response")
                                loading = false  // ✅ Stop loading before navigating
                                if (response.isSuccessful) {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                                    navController.navigate(Screen.OtherPage.Verification.bRoute)
                                } else {
                                    println("Registration failed: ${response.code()}")
                                }
                            } catch (e: Exception) {
                                loading = false
                                println("Network error: ${e.localizedMessage}")
                                Log.d("registering", " error: ${e.localizedMessage}")
                            }
                        }
                    } else {
                        // ❗ Show toast if passwords don't match


                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()

                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E3C8D))
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("REGISTER", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Already have an account? Login",
                color = Color.White,
                modifier = Modifier.clickable { navController.navigate(Screen.OtherPage.Login.bRoute) }
            )
        }
    }
}

private fun ColumnScope.onLoginClick() {
    TODO("Navigate to login screen")
}
