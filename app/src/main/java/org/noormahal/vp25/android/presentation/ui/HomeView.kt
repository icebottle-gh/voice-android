package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.presentation.navigation.HomeNavGraph
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope


@Composable
fun HomeView() {

//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope() //cause opening and closing drawer is a suspend function
    val mainViewModel: MainViewModel = viewModel()

    // Allow us to find out on which view we currently are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val currentScreen by mainViewModel.currentScreen.collectAsState()

    val title = if (currentScreen == Screen.BottomScreen.Stories) "Voice" else currentScreen.title


    val floatingButton :  @Composable () -> Unit = {
        if (currentScreen == Screen.BottomScreen.Stories){
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_create_24),
                    contentDescription = "New Story"
                )
            }
        }
//        else if (currentScreen == Screen.BottomScreen.Chats){
//            FloatingActionButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.baseline_message_24),
//                    contentDescription = "New Message"
//                )
//            }
//        }
    }



    Scaffold(
//        modifier = Modifier.safeDrawingPadding(),
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
                currentScreen = currentScreen,
                title = title,
            )
        },
        floatingActionButton = floatingButton,
        contentWindowInsets = WindowInsets.safeDrawing
        ){
        HomeNavGraph(navController = controller, mainViewModel = mainViewModel, pd = it)
    }
}

//
//@Preview
//@Composable
////fun MainViewPreview(){
////    MainView(it)
////}

