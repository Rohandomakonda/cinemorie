package com.example.netflix

import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.netflix.UserPreferences.AuthPreferences
import com.example.netflix.dtos.Profile
import com.example.netflix.retrofit.authApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val initialImages = listOf(
    R.drawable.black_panther,
    R.drawable.hulk,
    R.drawable.red_hulk,
    R.drawable.billy,
    R.drawable.iron_man
)


@Composable
fun profile(navController: NavController) {

    val profileList = remember { mutableStateOf<List<Profile>>(emptyList()) }
    val context = LocalContext.current
    val authPreferences = AuthPreferences(context)
    val auth by authPreferences.authData.collectAsState(initial = null)
    val userId= auth?.id
    LaunchedEffect(userId) {
        try {
            val response = userId?.let { authApi.getprofiles(it) }
            if (response != null) {
                if (response.isSuccessful) {
                    if (response != null) {
                        profileList.value = response.body() ?: emptyList()
                    }
                } else {
                    if (response != null) {
                        Toast.makeText(context, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
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
                        CircleImage3DEffect(profile,navController)
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
                                navController.navigate(Screen.OtherPage.AddProfile.bRoute)
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
fun CircleImage3DEffect(item: Profile,navController: NavController,authPreferences: AuthPreferences = AuthPreferences(LocalContext.current)) {
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
                    painter = painterResource(id = initialImages[item.avatar.toInt()]),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                        .clickable {
                            CoroutineScope(Dispatchers.IO).launch {
                                authPreferences.saveProfile(item)
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.BottomScreen.Home.bRoute)
                                }
                            }

                        }
                )
            }


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
                            .clickable {
                                // Since saveProfile is a suspend function, you need to call it from a coroutine scope
                                CoroutineScope(Dispatchers.IO).launch {
                                    authPreferences.saveProfile(item)
                                    withContext(Dispatchers.Main) {
                                        navController.navigate(Screen.OtherPage.EditProfile.bRoute)
                                    }
                                }
                            }


                    )
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
