package com.dvtopenweather.dvtopenweatherapp.ui.stateHolder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun appStateSetup(
    navHostController: NavHostController = rememberNavController()
): AppState {
    return AppState(navHostController)
}

@Stable
class AppState(
    val navHostController: NavHostController
) {

    val currentDestination: NavDestination?
        @Composable get() = navHostController.currentBackStackEntryAsState().value?.destination

    // used by bottom navigation to navigate to different screens
    fun navigateToDestination(destinationRoute: String) {
        navHostController.navigate(destinationRoute) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }
    }
}
