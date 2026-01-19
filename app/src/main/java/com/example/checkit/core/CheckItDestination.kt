package com.example.checkit.core

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface CheckItDestination {
    val icon: Int
    val route: String
    val showNavigationBar : Boolean
}

object Login: CheckItDestination{
    override val icon = 1
    override val route = "login"
    override val showNavigationBar = false
}

object Registration: CheckItDestination {
    override val icon = 1
    override val route = "registration"
    override val showNavigationBar = false
}

object NewChallenge: CheckItDestination {
    override val icon = 1
    override val route = "newChallenge"
    override val showNavigationBar = true
}

object ChallengeDetail : CheckItDestination {
    override val icon = 1
    override val route = "challengeDetail/{challengeId}"
    val arguments = listOf(navArgument("challengeId") { type = NavType.LongType })
    override val showNavigationBar = true
}



object TaskDetail : CheckItDestination{
    override val icon = 1
    override val route = "taskDetail/{taskId}?answer={answer}"
    val arguments = listOf(navArgument("taskId") { type = NavType.LongType },
        navArgument("answer"){type = NavType.StringType})
    val deepLink = listOf(navDeepLink { uriPattern = "checkit://completeTask/{taskId}?answer={answer}" })

    override val showNavigationBar = true
}


object Profile : CheckItDestination{
    override val icon = 1
    override val route = "profile"
    override val showNavigationBar = true

}

object MyChallengeList : CheckItDestination {
    override val icon = 1
    override val route = "myChallengeList"
    override val showNavigationBar = true
}

object ExploreChallenges : CheckItDestination {
    override val icon = 1
    override val route = "ExploreChallenges"
    override val showNavigationBar = true
}

fun allCheckItDestination(): List<CheckItDestination>{
    return listOf<CheckItDestination>(Login, Registration,Profile, NewChallenge, ChallengeDetail, TaskDetail,
        MyChallengeList, ExploreChallenges)
}