package com.example.checkit.core

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.checkit.core.ui.theme.CheckItTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckItTheme {
                CheckItApp()
            }
        }
    }
}

//@PreviewScreenSizes
@Composable
fun CheckItApp() {
    val navController = rememberNavController()

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        allCheckItDestination().find { it.route == currentDestination?.route } ?: Login


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen.showNavigationBar){
                NavigationBar() {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text("Perfil") },
                        selected = false,
                        onClick = {
                            navController.navigate(route = Profile.route) { launchSingleTop = true }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text("Desafio") },
                        selected = false,
                        onClick = {
                            navController.navigate(route = ChallengeTasks.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

        }
    ) { innerPadding ->
        CheckItNavHost(
            navController = navController,
            innerPadding = innerPadding

        )

    }


}



