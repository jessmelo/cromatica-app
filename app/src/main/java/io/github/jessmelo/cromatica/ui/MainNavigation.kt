package io.github.jessmelo.cromatica.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.ui.theme.screens.HomeScreen

@Composable
fun CromaticaApp(audioProcessor: AudioProcessor) {
    val navController = rememberNavController()
    Scaffold {
        MainNavigation(audioProcessor, navController, innerPadding = it)
    }
}

@Composable
fun MainNavigation(
    audioProcessor: AudioProcessor,
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(audioProcessor)
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
}
