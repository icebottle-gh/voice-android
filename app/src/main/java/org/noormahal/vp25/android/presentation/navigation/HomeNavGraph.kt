package org.noormahal.vp25.android.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.noormahal.vp25.android.presentation.ui.FindScreen
import org.noormahal.vp25.android.presentation.ui.OwnProfileView
import org.noormahal.vp25.android.presentation.ui.PersonProfileView
import org.noormahal.vp25.android.presentation.ui.StoriesDetail
import org.noormahal.vp25.android.presentation.ui.StoriesList
import org.noormahal.vp25.android.presentation.viewmodel.FindScreenViewModel
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel

@Composable
fun HomeNavGraph(
    navController: NavController,
    pd: PaddingValues,
    storiesViewModel: StoriesViewModel = viewModel(),
    findScreenViewModel: FindScreenViewModel = viewModel()
){
    NavHost(
        navController = navController as NavHostController,
//        startDestination = Screen.Login.route,
        startDestination = Screen.BottomScreen.Stories.bottomRoute,
        modifier = Modifier.padding(pd),
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ){
//        composable(Screen.Login.route){
//            LoginView(navController = navController)
//        }
        composable(Screen.BottomScreen.Stories.bottomRoute){
            StoriesList(navController,storiesViewModel)
        }
        composable(Screen.BottomScreen.Chats.bottomRoute){
//            ChatsInbox()
            run {  }
        }
        composable(Screen.BottomScreen.Alerts.bottomRoute){
//            Alerts()
            run {  }
        }
        composable(Screen.BottomScreen.Find.bottomRoute){
//            FindPeople()
            FindScreen(navController, findScreenViewModel)
        }
        composable(Screen.BottomScreen.Profile.bottomRoute){
            OwnProfileView()
        }
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
            PersonProfileView(userId = userId)
        }
    }
}
