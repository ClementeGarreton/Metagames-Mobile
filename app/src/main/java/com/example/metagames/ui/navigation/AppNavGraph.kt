package com.example.metagames.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.metagames.data.repository.AuthRepository
import com.example.metagames.ui.screens.auth.LoginScreen
import com.example.metagames.ui.screens.auth.SignupScreen
import com.example.metagames.ui.screens.home.HomeScreen
import com.example.metagames.ui.screens.game.GameScreen
import com.example.metagames.ui.screens.winners.WinnersScreen
import com.example.metagames.ui.screens.luck.LuckScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.HOME
) {
    val context = LocalContext.current
    val authRepo = AuthRepository(context.applicationContext as Application)

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
            GameScreen(
                onBackToHome = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }

        composable(Routes.WINNERS) {
            WinnersScreen()
        }

        composable(Routes.LUCK) {
            LuckScreen()
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                },
                onNavigateToSignup = {
                    navController.navigate(Routes.SIGNUP)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onSignupSuccess = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}