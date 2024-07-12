package io.github.jessmelo.cromatica.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jessmelo.cromatica.ui.theme.screens.HomeScreen

@Composable
fun CromaticaApp() {
    val navController = rememberNavController()
    Scaffold(

    ) {
        MainNavigation(navController, innerPadding = it)
    }
}

@Composable
fun MainNavigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
}