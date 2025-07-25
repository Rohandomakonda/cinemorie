package com.example.netflix


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument

@Composable
fun Main(controller: NavController) {
    // Get the current route from NavController's back stack

    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val violetColors = listOf(

        Color(0xFF4C1D95),
        Color(0xFF1E1B4B),  // Dark Violet 500// Violet 700

    )
    val darkVioletColors = violetColors.map { darken(it, 0.3f) }

    // Bottom Bar (could be shown based on certain conditions)
    val bottomBar: @Composable () -> Unit = {

            // Wrap NavigationBar in a Box to apply gradient background
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .background(
                        darken(Color(0xFF1E1B4B), 0.5f),
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
if(currentRoute !in introScreens.map{it.route}) {
    Scaffold(
        topBar = { appbar(navController = controller) },
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
    else{
        Navigation(navController = controller)
    }




}


@Composable
fun Navigation(navController: NavController) {

    NavHost(navController = navController as NavHostController, startDestination = Screen.OtherPage.Welcome.bRoute) {
        composable(Screen.OtherPage.Welcome.bRoute){
            WelcomeScreen(navController)
        }
        composable(Screen.BottomScreen.Home.bRoute) {
            HomeScreen(navController)
        }
       composable(Screen.BottomScreen.Movies.bRoute){
           Movies(navController)

       }
        composable(Screen.BottomScreen.Shows.bRoute){
            TvShows(navController)

        }
        composable(Screen.BottomScreen.WatchList.bRoute){
            WatchList(navController)

        }
        composable(  Screen.OtherPage.Genre.bRoute){
            GenrePage(navController)

        }
        composable( Screen.OtherPage.MovieInfo.bRoute){
                MovieDetailScreen( navController)

        }
        composable( Screen.OtherPage.ShowInfo.bRoute){
           ShowDetailScreen(navController)
        }

        composable(Screen.OtherPage.Login.bRoute){
            LoginPage(navController)
        }
        composable(Screen.OtherPage.Register.bRoute){
            RegisterPage(navController)
        }
        composable(Screen.OtherPage.Verification.bRoute){
            verificationpage(navController)
        }
        composable(Screen.OtherPage.Search.bRoute){
            Search(navController)
        }
        composable(Screen.OtherPage.AddProfile.bRoute){
            AddProfile(navController)
        }
        composable(Screen.OtherPage.EditProfile.bRoute){
            EditProfile(navController)
        }

        composable(Screen.OtherPage.Profile.bRoute){
            profile(navController)
        }

        composable(
            Screen.OtherPage.FullVideoScreen.bRoute
        ) {

                FullScreenVideoScreen(navController)

        }
        composable(
            Screen.OtherPage.ShowFullScreenVideo.bRoute
        ) {

            ShowFullScreenVideo(navController)

        }
        composable(
            route = Screen.OtherPage.WatchParty.bRoute,
            arguments = listOf(navArgument("partyCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyCode = backStackEntry.arguments?.getString("partyCode") ?: ""
            WatchPartyScreen(
                navController = navController,
                partyCode = partyCode
            )
        }



    }
}





