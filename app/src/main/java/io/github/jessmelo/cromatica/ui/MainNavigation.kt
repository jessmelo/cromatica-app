package io.github.jessmelo.cromatica.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jessmelo.cromatica.R
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.ui.screens.about.AboutScreen
import io.github.jessmelo.cromatica.ui.screens.guitartuner.GuitarTuner
import io.github.jessmelo.cromatica.ui.screens.home.HomeScreen
import io.github.jessmelo.cromatica.ui.theme.Lilac
import io.github.jessmelo.cromatica.ui.theme.Rosewater
import io.github.jessmelo.cromatica.ui.theme.RosewaterLight

@Composable
fun CromaticaApp(audioProcessor: AudioProcessor) {
    val navController = rememberNavController()
    val navItems = listOf(
        Pair(Screen.Home.route, "Home"),
        Pair(Screen.GuitarTuner.route, "Guitar Tuner"),
        Pair(Screen.Metronome.route, "Metronome"),
        Pair(Screen.About.route, "About")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Lilac,
                contentColor = Rosewater,
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
                            when (item.first) {
                                Screen.Home.route -> Icon(
                                    Icons.Filled.Home,
                                    contentDescription = null
                                )

                                Screen.GuitarTuner.route -> Icon(
                                    painter = painterResource(id = R.drawable.guitar_icon),
                                    modifier = Modifier.width(24.dp),
                                    contentDescription = null
                                )

                                Screen.Metronome.route -> Icon(
                                    Icons.Filled.PlayArrow,
                                    contentDescription = null
                                )

                                Screen.About.route -> Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null
                                )
                            }
                        },
                        label = { Text(text = item.second) },
                        selected = navController.currentDestination?.route == item.first,
                        onClick = { navController.navigate(item.first) }
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
        composable(Screen.GuitarTuner.route) {
            GuitarTuner(audioProcessor)
        }
        composable(Screen.Metronome.route) {
            Text("Metronome")
        }
        composable("about") {
            AboutScreen()
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object GuitarTuner : Screen("guitar-tuner")
    data object Metronome : Screen("metronome")
    data object About : Screen("about")
}
