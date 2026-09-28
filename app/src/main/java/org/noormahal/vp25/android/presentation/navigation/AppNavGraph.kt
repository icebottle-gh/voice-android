package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.components.rememberDelayedLoading
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

    val showLoadingSpinner = rememberDelayedLoading(isLoadingSession)

    if (isLoadingSession) {
        if (showLoadingSpinner) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    var hasHandledInitialRouting by remember { mutableStateOf(false) }
    LaunchedEffect(isLoggedIn) {
        if (hasHandledInitialRouting && !isLoggedIn) {
            // popUpTo(0) clears the whole back stack regardless of which screen logout
            // was triggered from (home, account setup, or the unverified landing page).
            navController.navigate("login") {
                popUpTo(0)
            }
        }
        hasHandledInitialRouting = true
    }

    NavHost(navController = navController, startDestination = if (isLoggedIn) "home" else "login") {
        composable("login") {
            LoginView (
                onLoginSuccess = { destination ->
                    val route = when (destination) {
                        PostAuthDestination.ACCOUNT_SETUP -> "account_setup"
                        PostAuthDestination.UNVERIFIED -> "unverified"
                        PostAuthDestination.HOME -> "home"
                    }
                    navController.navigate(route) {
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