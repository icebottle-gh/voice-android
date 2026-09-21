package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.presentation.ui.AccountSetupView
import org.noormahal.vp25.android.presentation.ui.HomeView
import org.noormahal.vp25.android.presentation.ui.LoginView
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel

@Composable
fun AppNavGraph(
    loginviewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by loginviewModel.isLoggedIn.collectAsState()
    val isLoadingSession by loginviewModel.isLoadingSession.collectAsState()

    if (isLoadingSession) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = if (isLoggedIn) "home" else "login") {
        composable("login") {
            LoginView (
                onLoginSuccess = {
                    loginviewModel.setLoggedIn(true)
                    navController.navigate("account_setup") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                loginviewModel
            )
        }

        composable("home") {
            HomeView()
        }

        composable("account_setup"){
            AccountSetupView {
                navController.navigate("home") {
                    popUpTo("account_setup") { inclusive = true }
                }
            }
        }
    }
}