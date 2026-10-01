package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.presentation.ui.FindScreen
import org.noormahal.vp25.android.presentation.ui.HomeShellScaffold
import org.noormahal.vp25.android.presentation.ui.OwnProfileScreen
import org.noormahal.vp25.android.presentation.ui.PersonProfileScreen
import org.noormahal.vp25.android.presentation.ui.StoriesDetail
import org.noormahal.vp25.android.presentation.ui.StoriesListScreen
import org.noormahal.vp25.android.presentation.viewmodel.FindScreenViewModel
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel

@Composable
fun HomeNavGraph(
    navController: NavController,
    currentRoute: String?,
    onBottomScreenClick: (Screen) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    storiesViewModel: StoriesViewModel = viewModel(),
    findScreenViewModel: FindScreenViewModel = viewModel()
){
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.Stories.bottomRoute,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ){
        composable(Screen.BottomScreen.Stories.bottomRoute){
            StoriesListScreen(navController, storiesViewModel, currentRoute, onBottomScreenClick)
        }
        // Chats is not part of the first release - route unreachable since it's also
        // removed from screensWithBottom in Screen.kt, kept here for when it ships.
//        composable(Screen.BottomScreen.Chats.bottomRoute){
//            ChatsInbox()
//            run {  }
//        }
        composable(Screen.BottomScreen.Alerts.bottomRoute){
            AlertsScreen(currentRoute, onBottomScreenClick)
        }
        composable(Screen.BottomScreen.Find.bottomRoute){
            FindScreen(navController, findScreenViewModel, currentRoute, onBottomScreenClick)
        }
        composable(Screen.BottomScreen.Profile.bottomRoute){
            OwnProfileScreen(currentRoute, onBottomScreenClick, onLogout)
        }

        // Escapes the shell chrome entirely - StoriesDetail owns its own full-bleed Scaffold
        // and hides the system status bar, so it must not sit inside HomeShellScaffold.
        composable(
            Screen.StoriesDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ){
            val userId = it.arguments?.getString("userId")?:""
            StoriesDetail(userId,storiesViewModel, navController)
        }

        composable(
            Screen.PersonProfile.route,
            arguments = listOf(navArgument("userId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            PersonProfileScreen(userId, currentRoute, onBack)
        }
    }
}

// Placeholder until the Alerts feature exists - keeps the shell chrome (title/actions) that
// screen will eventually want, colocated here since there's no dedicated Alerts screen file yet.
@Composable
private fun AlertsScreen(currentRoute: String?, onBottomScreenClick: (Screen) -> Unit) {
    HomeShellScaffold(
        currentRoute = currentRoute,
        title = "Alerts",
        onBottomScreenClick = onBottomScreenClick,
        actions = listOf(
            VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
            VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
            VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
        )
    ) { }
}
