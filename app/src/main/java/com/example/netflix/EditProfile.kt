package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview
@Composable
fun EditProfile(navController: NavController) {
    val violetColors = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetColors.map { darken(it, 0.3f) }
    val initialImages = remember {
        mutableStateListOf(
            R.drawable.black_panther,
            R.drawable.hulk,
            R.drawable.red_hulk,
            R.drawable.billy,
            R.drawable.iron_man
        )
    }
    val selectedIndex = remember { mutableStateOf(0) }
    var profileName by remember { mutableStateOf("") }
    val showSlider = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient(true, darkVioletColors)),
        contentAlignment = Alignment.TopCenter,
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.OtherPage.Profile.bRoute)
                    },
                    color = Color.White,
                    fontSize = 20.sp
                )

                Text(
                    text = "Done",
                    modifier = Modifier.clickable {

                    },
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp), // adjust this value as needed
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Edit Profile",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (!showSlider.value) {

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        contentAlignment = Alignment.TopEnd,
                        modifier = Modifier.clickable { showSlider.value = true }
                    ) {
                        Image(
                            painter = painterResource(initialImages[selectedIndex.value]),
                            contentDescription = "Selected Profile Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Gray, CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(initialImages) { index, url ->
                            val isSelected =
                                remember { derivedStateOf { selectedIndex.value == index } }

                            Box(contentAlignment = Alignment.TopEnd) {
                                Image(
                                    painter = painterResource(url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray, CircleShape)
                                        .border(2.dp, Color.White, CircleShape)
                                        .clickable {
                                            selectedIndex.value = index
                                            showSlider.value =
                                                false // Optionally close slider on selection
                                        },
                                    contentScale = ContentScale.Crop
                                )
                                if (isSelected.value) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Selected",
                                            tint = Color.Black,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = profileName,
                    onValueChange = { profileName = it },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    textStyle = TextStyle(color = Color.White),
                    label = { Text("Profile Name", color = Color.White)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified)
                )
            }
        }
    }
}