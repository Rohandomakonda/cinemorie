package com.example.netflix


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHost as NavHost1

@Composable
fun Main() {
    // Get the current route from NavController's back stack
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val violetColors = listOf(

        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),  // Dark Violet 500// Violet 700

    )
    val darkVioletColors = violetColors.map { darken(it, 0.3f) }

    // Bottom Bar (could be shown based on certain conditions)
    val bottomBar: @Composable () -> Unit = {
        if (currentRoute == "home") {
            // Wrap NavigationBar in a Box to apply gradient background
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .background(
                        darken(Color(0xFF1E1B4B),0.5f),
                    )
            ) {
                NavigationBar(
                    containerColor = Color.Transparent, // Make NavigationBar itself transparent
                    tonalElevation = 0.dp, // No shadow/elevation for clean gradient look
                    modifier = Modifier.fillMaxSize() // Fill the Box
                ) {
                    screensInBottom.forEach { item ->
                        val isSelected = currentRoute == item.bRoute


                        NavigationBarItem(
                           selected = false,
                            onClick = {
                                controller.navigate(item.bRoute)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.bTitle,
                                    modifier = Modifier.size(24.dp),
                                    tint = if( isSelected ){Color.White} else {Color.Gray}
                                )
                            },
                            label = {
                                Text(
                                    text = item.bTitle,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    color = if( isSelected ){Color.White} else {Color.Gray}
                                )
                            },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    }


    Scaffold(
        topBar = { appbar() },
        bottomBar = { bottomBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(gradient(isVertical = true, colors = darkVioletColors)),
        ) {
            Navigation(navController = controller)
        }

    }
}
@Composable
fun Navigation(navController: NavController) {

    NavHost(navController = navController as NavHostController, startDestination = Screen.BottomScreen.Home.route) {
        composable(Screen.BottomScreen.Home.bRoute) {
            HomeScreen()
        }

    }
}




