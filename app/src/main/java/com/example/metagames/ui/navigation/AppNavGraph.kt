package com.example.metagames.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.metagames.ui.screens.home.HomeScreen
import com.example.metagames.ui.screens.game.GameScreen
import com.example.metagames.ui.screens.winners.WinnersScreen
import com.example.metagames.ui.screens.luck.LuckScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(Routes.GAME) {
            GameScreen()
        }

        composable(Routes.WINNERS) {
            WinnersScreen()
        }

        composable(Routes.LUCK) {
            LuckScreen()
        }
    }
}
