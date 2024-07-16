package io.github.jessmelo.cromatica.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.ui.screens.HomeScreen
import io.github.jessmelo.cromatica.ui.theme.Lilac
import io.github.jessmelo.cromatica.ui.theme.Rosewater
import io.github.jessmelo.cromatica.ui.theme.RosewaterLight


@Composable
fun CromaticaApp(audioProcessor: AudioProcessor) {
    val navController = rememberNavController()
    val navItems = listOf("Home", "Guitar", "Metronome")
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Lilac,
                contentColor = Rosewater
            ) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Rosewater,
                            selectedTextColor = Rosewater,
                            unselectedIconColor = RosewaterLight,
                            unselectedTextColor = RosewaterLight
                        ),
                        icon = {
                            if (item == "Home") {
                                Icon(Icons.Filled.Home, contentDescription = null)
                            } else if (item == "Guitar") {
                                Icon(Icons.Filled.Star, contentDescription = null)
                            } else {
                                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            }
                        },
                        label = { Text(text = item) },
                        selected = navController.currentDestination?.route == item,
                        onClick = { navController.navigate(item) }
                    )
                }
            }
        }
    ) {
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
