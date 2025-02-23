package com.example.temp.presentation.ui

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.temp.MainViewModel
import com.example.temp.R
import com.example.temp.presentation.navigation.Navigation
import com.example.temp.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope


@Composable
fun MainView(){

//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope() //cause opening and closing drawer is a suspend function
    val viewModel: MainViewModel = viewModel()

    // Allow us to find out on which view we currently are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val currentScreen by viewModel.currentScreen.collectAsState()

    val title = if (currentScreen == Screen.BottomScreen.Stories) "Voice" else currentScreen.title


    val floatingButton :  @Composable () -> Unit = {
        if (currentScreen == Screen.BottomScreen.Chats){
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_message_24),
                    contentDescription = "New Message"
                )
            }
        }else if (currentScreen == Screen.BottomScreen.Stories){
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_create_24),
                    contentDescription = "New Story"
                )
            }
        }
    }


    Scaffold(
        bottomBar = {
            BottomBarView(
                currentScreen = currentScreen,
//                viewModel = viewModel,
                currentRoute = currentRoute,
                controller = controller
            )
        },
        topBar = {
            AppBarView(
                title = title,
            )
        },
        floatingActionButton = floatingButton

        ){
        Navigation(navController = controller, viewModel = viewModel, pd = it)
    }
}


@Preview
@Composable
fun MainViewPreview(){
    MainView()
}

