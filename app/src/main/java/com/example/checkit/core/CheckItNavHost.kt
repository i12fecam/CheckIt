package com.example.checkit.core

import ProfileDetailScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.checkit.features.challenges.ui.MyChallengeListScreen
import com.example.checkit.features.challenges.ui.NewChallengeScreen
import com.example.checkit.features.registration.ui.LoginScreen
import com.example.checkit.features.registration.ui.RegistrationScreen
import com.example.checkit.features.challenges.ui.ChallengeDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun CheckItNavHost(navController: NavHostController,innerPadding: PaddingValues){

    NavHost(
        navController = navController,
        startDestination = Login.route,
        modifier = Modifier.padding(innerPadding))
    {
        composable(route = ChallengeTasks.route){
            ChallengeListScreen(message = null)
        }
        composable(route = Login.route) {
            LoginScreen(onLogin = {
                navController.navigate(ChallengeTasks.route)

            },
            onNavigateToRegistration = {
                navController.navigate(Registration.route)
            })
        }
        composable(route = Registration.route){
            RegistrationScreen(
                onRegister = {
                    navController.navigate(Login.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Login.route)
                }
            )
        }

        composable( route = NewChallenge.route){
            NewChallengeScreen(onCreation = { id ->
                navController.navigate("challenge_detail/$id"){
                    popUpTo(NewChallenge.route){ inclusive = true }
                }
            },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // NUOVA ROTTA: Dettaglio Sfida (Ruta Romana)
        composable(
            route = "challenge_detail/{challengeId}",
            arguments = listOf(navArgument("challengeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("challengeId") ?: 0L
            ChallengeDetailScreen(
                onBack = { navController.popBackStack() }
                // Passa l'id al ViewModel del dettaglio se necessario
            )
        }

        composable(route = MyChallengeList.route) {
            MyChallengeListScreen(
                onChallengeClick = { challengeId ->
                    // Aquí defines a dónde ir cuando el usuario toca un desafío
                    // Usamos el ID (Long) para navegar al detalle o tareas
                    navController.navigate("${ChallengeTasks.route}/$challengeId")
                }
            )
        }


        composable(route = Profile.route){
            ProfileDetailScreen(
                onLogin ={},
                onNavigateToRegistration ={},
            )
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
