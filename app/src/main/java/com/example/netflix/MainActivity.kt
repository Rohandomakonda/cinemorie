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
import androidx.navigation.compose.rememberNavController
import com.example.netflix.ui.theme.NetflixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetflixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val controller: NavController = rememberNavController()
                    val currentRoute =  Screen.OtherPage.ShowInfo.bRoute
                    if (currentRoute in introScreens.map { it.route }) {
                        Navigation1(navController = controller)
                    }
                    else {
                        Main()
                    }

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

