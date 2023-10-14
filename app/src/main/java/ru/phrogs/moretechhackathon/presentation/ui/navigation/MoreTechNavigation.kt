package ru.phrogs.moretechhackathon.presentation.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object MoreTechDestinations {
    const val ONBOARDING = "onboarding"
    const val SERVICE_SELECTION = "service_selection"
    const val DISABILITY_SELECTION = "disability_selection"
    const val MAIN = "main"
    const val SEARCH_HISTORY = "search_history"
    const val CHAT = "chat"
}

@androidx.compose.runtime.Composable
fun MoreTechNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MoreTechDestinations.ONBOARDING
    ) {
        composable(MoreTechDestinations.ONBOARDING) {
            // TODO
        }
        composable(MoreTechDestinations.SERVICE_SELECTION) {
            // TODO
        }
        composable(MoreTechDestinations.DISABILITY_SELECTION) {
            // TODO
        }
        composable(MoreTechDestinations.MAIN) {
            // TODO
        }
        composable(MoreTechDestinations.SEARCH_HISTORY) {
            // TODO
        }
        composable(MoreTechDestinations.CHAT) {
            // TODO
        }
    }
}