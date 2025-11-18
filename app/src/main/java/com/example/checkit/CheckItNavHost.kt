package com.example.checkit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun CheckItNavHost(navController: NavHostController,innerPadding: PaddingValues){

    NavHost(
        navController = navController,
        startDestination = ChallengeTasks.route,
        modifier = Modifier.padding(innerPadding))
    {
        composable(route = ChallengeTasks.route){
            ChallengeListScreen(message = null)
        }
        composable(route = Profile.route) {
            LoginScreen(onLoginSuccess = null)
        }
        composable(
            route = ChallengeTasks.routeWithArgs,
            arguments = ChallengeTasks.arguments,
            deepLinks= ChallengeTasks.deepLinks
        ){
                navBackStackEntry ->
            val message = navBackStackEntry.arguments?.getString(ChallengeTasks.nombreTareaArg)
            ChallengeListScreen(message?: "Tarea 3")
        }
    }
}
