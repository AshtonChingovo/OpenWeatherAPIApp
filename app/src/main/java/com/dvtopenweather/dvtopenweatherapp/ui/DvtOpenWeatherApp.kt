package com.dvtopenweather.dvtopenweatherapp.ui

import BottomNavigationMenuItem
import OpenWeatherAppNavHost
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.ui.screens.locationPermissions.LocationPermissions
import com.dvtopenweather.dvtopenweatherapp.ui.stateHolder.appStateSetup
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DvtOpenWeatherApp(
    networkMonitorViewModel: NetworkMonitorViewModel = hiltViewModel()
) {

    val appState = appStateSetup()
    val navHostStartDestination = stringResource(R.string.home_screen)

    val snackbarIssuesState = networkMonitorViewModel.internetConnectivity.collectAsStateWithLifecycle(false).value
    val snackbarHostState = remember { SnackbarHostState() }

    val message = stringResource(R.string.internet_connection_unavailable)

    LaunchedEffect(snackbarIssuesState) {
        if (!snackbarIssuesState) {
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Indefinite)
        }
    }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // list of bottom bar screen destinations
    val bottomBarMenuItemsScreenDestinationsList = listOf(
        BottomNavigationMenuItem.Home,
        BottomNavigationMenuItem.Favourites,
        BottomNavigationMenuItem.Map,
        BottomNavigationMenuItem.About
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        if (locationPermissionsState.allPermissionsGranted) {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    BottomBar(
                        bottomBarMenuItemsScreenDestinationsList,
                        appState.currentDestination,
                        appState::navigateToDestination
                    )
                }
            ) {
                OpenWeatherAppNavHost(
                    navController = appState.navHostController,
                    startDestination = navHostStartDestination,
                    Modifier.padding(it)
                )
            }
        } else {
            LocationPermissions(locationPermissionsState)
        }

    }
}

@Composable
fun BottomBar(
    destinations: List<BottomNavigationMenuItem>,
    currentNavDestination: NavDestination?,
    onClick: (String) -> Unit
) {
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.Black,
        elevation = 0.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .background(color = Color.White)

    ) {
        destinations.forEach { destination ->

            val navRoute = stringResource(id = destination.route)

            val selected = currentNavDestination?.hierarchy?.any { it.route?.contains(navRoute) ?: false } ?: false

            BottomNavigationItem(
                selected = selected,
                label = { Text(stringResource(id = destination.menuTitleResourceId)) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selected) destination.selectedIcon else destination.unselectedIcon
                        ),
                        contentDescription = null
                    )
                },
                onClick = { onClick(navRoute) }
            )
        }
    }
}
