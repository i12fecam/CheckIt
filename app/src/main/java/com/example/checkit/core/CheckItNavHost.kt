package com.example.checkit.core

import ProfileDetailScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.checkit.features.challenges.ui.ExploreChallengesScreen
import com.example.checkit.features.challenges.ui.MyChallengeListScreen
import com.example.checkit.features.challenges.ui.NewChallengeScreen
import com.example.checkit.features.registration.ui.LoginScreen
import com.example.checkit.features.registration.ui.RegistrationScreen
import com.example.checkit.features.challenges.ui.ChallengeDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.checkit.features.challenges.ui.ChallengeTaskDetailScreen

@Composable
fun CheckItNavHost(navController: NavHostController,innerPadding: PaddingValues){

    NavHost(
        navController = navController,
        startDestination = Login.route,
        modifier = Modifier.padding(innerPadding))
    {


        composable(route = Login.route) {
            LoginScreen(onLogin = {
                navController.navigate(MyChallengeList.route)

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
                navController.navigate(ChallengeDetail.route.replace("{challengeId}", id.toString())){
                    popUpTo(NewChallenge.route){ inclusive = true }
                }
            },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ChallengeDetail.route,
            arguments = ChallengeDetail.arguments
        ) { backStackEntry ->
            //val id = backStackEntry.arguments?.getLong("challengeId") ?: 0L
            ChallengeDetailScreen(
                onBack = { navController.popBackStack() },
                onDeleteSuccess = {
                    // CAMBIA QUI: forza la navigazione verso Mis Desafíos e pulisce lo stack
                    navController.navigate(MyChallengeList.route) {
                        popUpTo(MyChallengeList.route) { inclusive = true }
                    }
                },
                onTaskClick = { id ->
                    navController.navigate(TaskDetail.route.replace("{taskId}", id.toString()))
                }
            )
        }
        
        composable(
            route = TaskDetail.route,
            arguments = TaskDetail.arguments,
            deepLinks = TaskDetail.deepLink
        ) { backStackEntry ->
            //val id = backStackEntry.arguments?.getLong("taskId") ?: 0L
            ChallengeTaskDetailScreen(
                onBack = {navController.popBackStack()},
            )
        }



        composable(route = MyChallengeList.route) {
            MyChallengeListScreen(
                onChallengeClick = { id ->
                    // Aquí defines a dónde ir cuando el usuario toca un desafío
                    // Usamos el ID (Long) para navegar al detalle o tareas
                    navController.navigate(ChallengeDetail.route.replace("{challengeId}", id.toString()))
                }
            )
        }
        composable(route = ExploreChallenges.route) {
            ExploreChallengesScreen(
                onChallengeClick = { id ->
                    navController.navigate(ChallengeDetail.route.replace("{challengeId}", id.toString()))
                }
            )
        }

        composable(route = Profile.route){
            ProfileDetailScreen(
                onCloseSession = {
                    navController.navigate(Login.route)
                },
            )
        }
    }
}
