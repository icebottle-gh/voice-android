package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.presentation.ui.AccountSetupView
import org.noormahal.vp25.android.presentation.ui.HomeView
import org.noormahal.vp25.android.presentation.ui.LoginView
import org.noormahal.vp25.android.presentation.ui.UnverifiedView
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel

@Composable
fun AppNavGraph(
    loginviewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by loginviewModel.isLoggedIn.collectAsState()
    val isLoadingSession by loginviewModel.isLoadingSession.collectAsState()
    val postAuthDestination by loginviewModel.postAuthDestination.collectAsState()

    if (isLoadingSession) {
        return
    }

    val initialDestination = remember {
        if (isLoggedIn) (postAuthDestination?.toRoute() ?: "home") else "login"
    }

    var hasHandledInitialRouting by remember { mutableStateOf(false) }
    LaunchedEffect(isLoggedIn) {
        if (hasHandledInitialRouting && !isLoggedIn) {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
        hasHandledInitialRouting = true
    }

    NavHost(navController = navController, startDestination = initialDestination) {
        composable("login") {
            LoginView (
                onLoginSuccess = { destination ->
                    navController.navigate(destination.toRoute()) {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                },
                loginviewModel
            )
        }

        composable("home") {
            HomeView(loginViewModel = loginviewModel)
        }

        composable("account_setup"){
            AccountSetupView { destination ->
                val route = if (destination == PostAuthDestination.UNVERIFIED) "unverified" else "home"
                navController.navigate(route) {
                    popUpTo("account_setup") { inclusive = true }
                }
            }
        }

        composable("unverified") {
            UnverifiedView(onLogout = { loginviewModel.logout() })
        }
    }
}