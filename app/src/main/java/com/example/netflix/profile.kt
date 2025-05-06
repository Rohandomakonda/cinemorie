package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class ProfileItem(
    val name: String,
    val imageResId: Int // Resource ID from R.drawable.*
)
@Composable
fun profile() {
    val profileList = listOf(
        ProfileItem("Alice", R.drawable.iron_man),
        ProfileItem("Bob", R.drawable.black_panther),
        ProfileItem("Charlie", R.drawable.red_hulk),
//        ProfileItem("Diana", R.drawable.billy),
//        ProfileItem("Ethan", R.drawable.hulk),
    )

    val violetColors = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),
    )
    val darkVioletColors = violetColors.map { darken(it, 0.3f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient(true, darkVioletColors))
            .padding(16.dp),
        contentAlignment = Alignment.Center // Center the grid within the Box
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Center content vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {


            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 columns
                verticalArrangement = Arrangement.spacedBy(8.dp), // Equal vertical space
                horizontalArrangement = Arrangement.Center, // Center content horizontally
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Optional padding for the grid
            ) {
                items(profileList) { profile ->
                    CircleImage3DEffect(name = profile.name, imageResId = profile.imageResId)
                }
            }

            // Add Icon in square shape at the bottom
           // Spacer(modifier = Modifier.weight(1f)) // This pushes the Add icon to the bottom
            Column( horizontalAlignment = Alignment.CenterHorizontally) {


                Box(
                    modifier = Modifier
                        .size(90.dp) // Same size as profile item
                        .clip(CircleShape) // Circle shape
                        .background(Color.DarkGray) // Darker background color
                        .border(
                            2.dp,
                            Color.LightGray,
                            CircleShape
                        ) // Border to match the profile item style
                        .clickable {
                            // Handle the add action here
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, // Add icon
                        contentDescription = "Add Profile",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Corrected the spelling of 'modifier'

                Text(
                    text = "Add profiles",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color.White // ✅ Make text white
                )
            }


        }
    }
}





@Composable
fun CircleImage3DEffect(name: String, imageResId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Color.Gray,
                    spotColor = Color.Black
                )
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color.White // ✅ Make text white
        )
    }
}
