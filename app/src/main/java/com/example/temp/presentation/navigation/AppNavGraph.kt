package com.example.temp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.temp.presentation.ui.HomeView
import com.example.temp.presentation.ui.LoginView
import com.example.temp.presentation.viewmodel.LoginViewModel

@Composable
fun AppNavGraph(
    viewModel: LoginViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val startDestination = if (isLoggedIn) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginView (
                onLoginSuccess = {
                    viewModel.setLoggedIn(true)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel
            )

        }

        composable("home") {
            HomeView()
        }
    }
}