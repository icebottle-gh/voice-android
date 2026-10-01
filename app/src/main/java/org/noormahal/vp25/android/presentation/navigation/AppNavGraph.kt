package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val sessionError by loginviewModel.sessionError.collectAsState()

    if (isLoadingSession) {
        return
    }

    if (!isLoggedIn && sessionError != null) {
        SessionResolutionError(message = sessionError.orEmpty(), onRetry = loginviewModel::retryResolveSession)
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
            val initialMobile by loginviewModel.setupMobile.collectAsState()
            AccountSetupView(initialMobile = initialMobile) { destination ->
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

@Composable
private fun SessionResolutionError(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}