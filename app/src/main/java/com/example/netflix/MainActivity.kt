package com.example.netflix


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.netflix.ui.theme.NetflixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetflixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
//                    val controller: NavController = rememberNavController()
//                    val currentRoute =  Screen.BottomScreen.Home.bRoute
//                    if (currentRoute in introScreens.map { it.route }) {
//                        Navigation1(navController = controller)
//                    }
//                    else {
//                        Main()
//                    }
                    val data =  showDetails(
                            title = "Harry Potter",
                    description = "blah blah",
                    tags = "A",
                    rating = 9.3,
                    year = 2001,
                    ageRating = "13+",
                    backgroundImage = "https://m.media-amazon.com/images/I/61wSaUwpR0L._AC_UF894,1000_QL80_.jpg",
                    episodes = listOf(
                        "Pilot" to (
                                "Rick moves in with his daughter's family and quickly exerts his influence over grandson Morty." to
                                        "https://rickandmortyapi.com/api/episode/1/image"
                                ),
                        "Lawnmower Dog" to (
                                "Rick and Morty enter the dreams of Morty's teacher to improve his grades." to
                                        "https://rickandmortyapi.com/api/episode/2/image"
                                ),
                        "Anatomy Park" to (
                                "Rick miniaturizes Morty and sends him into a homeless man’s body to save Anatomy Park." to
                                        "https://rickandmortyapi.com/api/episode/3/image"
                                ),
                        "M. Night Shaym-Aliens!" to (
                                "Rick, Morty, and Jerry are held captive in a virtual reality by aliens." to
                                        "https://rickandmortyapi.com/api/episode/4/image"
                                )
                    )
                    ,
                    morelikethis = listOf("Harry Potter" to "https://m.media-amazon.com/images/I/61wSaUwpR0L._AC_UF894,1000_QL80_.jpg","Google" to "google_icon.png"),
                    genres = listOf("Magic","Mystery","Sci-fi")
                    )
                    ShowDetailScreen(data)
                }
            }
        }
    }
}

@Composable
fun Navigation1(navController: NavController) {

    NavHost(navController = navController as NavHostController, startDestination = Screen.OtherPage.Welcome.route) {
        composable(Screen.OtherPage.Welcome.bRoute){
            WelcomeScreen(navController)
        }
        composable(Screen.OtherPage.AddProfile.bRoute){
            AddProfile()
        }
        composable(Screen.OtherPage.Login.bRoute){
            LoginPage(navController)
        }
        composable(Screen.OtherPage.Register.bRoute){
            RegisterPage(navController)
        }

        composable(Screen.OtherPage.Verification.bRoute){
            verificationpage()
        }

    }
}

