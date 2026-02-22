package org.noormahal.vp25.android.presentation.navigation

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
import org.noormahal.vp25.android.components.PersonProfileLongBioPreview
import org.noormahal.vp25.android.presentation.ui.FindScreen
import org.noormahal.vp25.android.presentation.ui.StoriesDetail
import org.noormahal.vp25.android.presentation.ui.StoriesList
import org.noormahal.vp25.android.presentation.viewmodel.FindScreenViewModel
import org.noormahal.vp25.android.presentation.viewmodel.MainViewModel
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel

@Composable
fun HomeNavGraph(
    navController: NavController,
    mainViewModel: MainViewModel,
    pd: PaddingValues,
    storiesViewModel: StoriesViewModel = viewModel(),
    findScreenViewModel: FindScreenViewModel = viewModel()
){
    NavHost(
        navController = navController as NavHostController,
//        startDestination = Screen.Login.route,
        startDestination = Screen.BottomScreen.Stories.bottomRoute,
        modifier = Modifier.padding(pd)
    ){
//        composable(Screen.Login.route){
//            LoginView(navController = navController)
//        }
        composable(Screen.BottomScreen.Stories.bottomRoute){
            mainViewModel.setCurrentScreen(Screen.BottomScreen.Stories.bottomRoute)
            StoriesList(navController,mainViewModel,storiesViewModel)
        }
//        composable(Screen.BottomScreen.Chats.bottomRoute){
////            ChatsInbox()
//            mainViewModel.setCurrentScreen(Screen.BottomScreen.Chats.bottomRoute)
//            run {  }
//        }
        composable(Screen.BottomScreen.Notifications.bottomRoute){
//            Notification()
            mainViewModel.setCurrentScreen(Screen.BottomScreen.Notifications.bottomRoute)
            run {  }
        }
        composable(Screen.BottomScreen.Find.bottomRoute){
//            FindPeople()
            mainViewModel.setCurrentScreen(Screen.BottomScreen.Find.bottomRoute)
            FindScreen(navController, findScreenViewModel)
        }
        composable(
            Screen.StoriesDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ){
            val userId = it.arguments?.getString("userId")?:""
            mainViewModel.setCurrentScreen(Screen.StoriesDetail.route)
            StoriesDetail(userId,storiesViewModel, navController)
        }

        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            PersonProfileLongBioPreview()
        }
    }
}
