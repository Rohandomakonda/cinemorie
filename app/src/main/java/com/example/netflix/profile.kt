package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.zIndex


data class ProfileItem(
    var name: String,
    var imageResId: Int,
    var isEditing: MutableState<Boolean>
)

@Composable
fun profile() {
    val profileList = remember {
        mutableStateOf(
            listOf(
                ProfileItem(name = "Alice", imageResId = R.drawable.iron_man, isEditing = mutableStateOf(false)),
                ProfileItem(name = "Bob", imageResId = R.drawable.black_panther, isEditing = mutableStateOf(false)),
                ProfileItem(name = "Charlie", imageResId = R.drawable.red_hulk, isEditing = mutableStateOf(false)),
                ProfileItem(name = "Diana", imageResId = R.drawable.billy, isEditing = mutableStateOf(false)),
                ProfileItem(name = "Ethan", imageResId = R.drawable.hulk, isEditing = mutableStateOf(false))
            )
        )
    }



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
                Text(
                    text = "Whose is Watching ?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp) // ✅ Make text white
                )


                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 columns
                    verticalArrangement = Arrangement.spacedBy(8.dp), // Equal vertical space
                    horizontalArrangement = Arrangement.Center, // Center content horizontally
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Optional padding for the grid
                ) {
                    items(profileList.value) { profile ->
                        CircleImage3DEffect(profile)
                    }
                }


                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                            .border(
                                2.dp,
                                Color.LightGray,
                                CircleShape
                            )
                            .clickable {

                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Profile",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Add profiles",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = Color.White
                    )
                }


            }
        }
    }




@Composable
fun CircleImage3DEffect(item: ProfileItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Gray,
                        spotColor = Color.Black
                    )
            ) {
                Image(
                    painter = painterResource(id = item.imageResId),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                )
            }

            if (!item.isEditing.value) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-10).dp)
                        .background(Color.Black, CircleShape)
                        .clickable {

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))


            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

    }
}
