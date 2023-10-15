package ru.phrogs.moretechhackathon.presentation.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.phrogs.moretechhackathon.feature_chat_bot.presentation.screen.ChatScreen
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.MapScreen
import ru.phrogs.moretechhackathon.presentation.ui.screen.onboarding.OnboardingScreen

object MoreTechDestinations {
    const val ONBOARDING = "onboarding"
    const val SERVICE_SELECTION = "service_selection"
    const val DISABILITY_SELECTION = "disability_selection"
    const val MAIN = "main"
    const val SEARCH_HISTORY = "search_history"
    const val CHAT = "chat"
}

@Composable
fun MoreTechNavigation(
    navController: NavHostController, context: Context
) {
    NavHost(
        navController = navController, startDestination = MoreTechDestinations.ONBOARDING
    ) {
        composable(MoreTechDestinations.ONBOARDING) {
            OnboardingScreen(navController = navController)
        }
        composable(MoreTechDestinations.SERVICE_SELECTION) {
            // TODO
        }
        composable(MoreTechDestinations.DISABILITY_SELECTION) {
            // TODO
        }
        composable(MoreTechDestinations.MAIN) {
            MapScreen(context = context, navController = navController)
        }
        composable(MoreTechDestinations.SEARCH_HISTORY) {
            // TODO
        }
        composable(MoreTechDestinations.CHAT) {
            ChatScreen()
        }
    }
}