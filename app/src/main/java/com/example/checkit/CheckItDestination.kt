package com.example.checkit

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface CheckItDestination {
    val icon: Int
    val route: String
}

object ChallengeTasks : CheckItDestination{
    override val icon = 1
    override val route = "challengeTasks"

    const val nombreTareaArg =  "nombretarea"
    val routeWithArgs = "${route}/{${nombreTareaArg}}"

    val deepLinks = listOf(navDeepLink { uriPattern =  "checkit://${route}/{${nombreTareaArg}}" })
    val arguments = listOf(
        navArgument(nombreTareaArg){type = NavType.StringType}
    )
}

object Profile : CheckItDestination{
    override val icon = 1
    override val route = "profile"
}

fun allCheckItDestination(): List<CheckItDestination>{
    return listOf<CheckItDestination>(ChallengeTasks,Profile)
}