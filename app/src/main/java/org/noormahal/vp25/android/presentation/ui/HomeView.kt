package org.noormahal.vp25.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.presentation.navigation.HomeNavGraph
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel
import org.noormahal.vp25.android.presentation.viewmodel.ProfileViewModel

@Composable
fun HomeView(
    loginViewModel: LoginViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        profileViewModel.fetchOwnProfile()
    }

    val controller: NavHostController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    HomeNavGraph(
        navController = controller,
        currentRoute = currentRoute,
        onBottomScreenClick = { screen ->
            controller.navigate(screen.route) {
                popUpTo(controller.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        onLogout = { loginViewModel.logout() },
        onBack = { controller.popBackStack() }
    )
}
