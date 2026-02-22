package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val isLoggedIn by loginviewModel.isLoggedIn.collectAsState()

    var splashComplete by remember { mutableStateOf(false) }

    val startDestination = "login"
//    val startDestination = if (isLoggedIn) "home" else "login"

//    LaunchedEffect(Unit) {
//        // Auto login check
//        AuthPrefs.getToken(context).collect { token ->
//            if (!token.isNullOrEmpty()) {
//                Client.token = token
//                loginViewModel.setLoggedIn(true)
//                navController.navigate("home") {
//                    popUpTo("splash") { inclusive = true }
//                }
//            } else {
//                navController.navigate("login") {
//                    popUpTo("splash") { inclusive = true }
//                }
//            }
//            splashComplete = true
//        }
//    }

    NavHost(navController = navController, startDestination = startDestination) {
//        composable("splash") {
//            SplashView()
//        }
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