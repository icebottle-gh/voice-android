package com.example.temp.presentation.navigation

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
import com.example.temp.presentation.ui.StoriesDetail
import com.example.temp.presentation.ui.StoriesList
import com.example.temp.presentation.viewmodel.MainViewModel
import com.example.temp.presentation.viewmodel.StoriesViewModel

@Composable
fun HomeNavGraph(
    navController: NavController,
    mainViewModel: MainViewModel,
    pd: PaddingValues,
    storiesViewModel: StoriesViewModel = viewModel()
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
            run {  }
        }
        composable(
            Screen.StoriesDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ){
            val userId = it.arguments?.getString("userId")?:""
            mainViewModel.setCurrentScreen(Screen.StoriesDetail.route)
            StoriesDetail(userId,storiesViewModel, navController)
        }
    }
}
