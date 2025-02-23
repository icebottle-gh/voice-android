package com.example.temp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.temp.MainViewModel
import com.example.temp.presentation.ui.StoriesDetail
import com.example.temp.presentation.ui.StoriesList

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues){
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.Stories.bottomRoute,
        modifier = Modifier.padding(pd)
    ){
        composable(Screen.BottomScreen.Stories.bottomRoute){
            viewModel.setCurrentScreen(Screen.BottomScreen.Stories.bottomRoute)
            StoriesList(navController,viewModel)
        }
        composable(Screen.BottomScreen.Chats.bottomRoute){
//            ChatsInbox()
            viewModel.setCurrentScreen(Screen.BottomScreen.Chats.bottomRoute)
            run {  }
        }
        composable(Screen.BottomScreen.Notifications.bottomRoute){
//            Notification()
            viewModel.setCurrentScreen(Screen.BottomScreen.Notifications.bottomRoute)
            run {  }
        }
        composable(Screen.BottomScreen.Find.bottomRoute){
//            FindPeople()
            viewModel.setCurrentScreen(Screen.BottomScreen.Find.bottomRoute)
            run {  }
        }
        composable(
            Screen.StoriesDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ){
            val postId = it.arguments?.getString("postId")
            viewModel.setCurrentScreen(Screen.StoriesDetail.route)
            StoriesDetail(postId ?: "")
        }
    }
}
