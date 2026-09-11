package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.components.FabStyle
import org.noormahal.vp25.android.components.VpFab
import org.noormahal.vp25.android.components.VpTopAppBar
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.presentation.navigation.HomeNavGraph
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.navigation.screensWithBottom
import org.noormahal.vp25.android.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.noormahal.vp25.android.theme.VpTheme


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

    HomeScreen(
        currentScreen = currentScreen,
        title = title,
        currentRoute = currentRoute,
        controller = controller
    ) { pd ->
        HomeNavGraph(navController = controller, mainViewModel = mainViewModel, pd = pd)
    }
}

@Composable
private fun HomeScreen(
    currentScreen: Screen,
    title: String,
    currentRoute: String?,
    controller: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val floatingButton: @Composable () -> Unit = {
        if (currentScreen == Screen.BottomScreen.Stories){
            VpFab(
                icon = ImageVector.vectorResource(id = R.drawable.baseline_create_24),
                contentDescription = "New Story",
                onClick = { /*TODO*/ },
                style = FabStyle.SQUIRCLE_PRIMARY
            )
        }
//        else if (currentScreen == Screen.BottomScreen.Chats){
//            VpFab(
//                icon = ImageVector.vectorResource(id = R.drawable.baseline_message_24),
//                contentDescription = "New Message",
//                onClick = { /*TODO*/ },
//                style = FabStyle.SQUIRCLE_PRIMARY
//            )
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
            //so as to control visibility based on diff situations BACK OR DRAWER
            if (currentScreen in screensWithBottom) {
                VpTopAppBar(
                    title = title,
                    leadingIcon = if (currentScreen == Screen.BottomScreen.Stories) {
                        Icons.Default.AccountCircle
                    } else {
                        Icons.Filled.ArrowBack
                    },
                    leadingIconContentDescription = if (currentScreen == Screen.BottomScreen.Stories) "Menu" else null,
                    onLeadingIconClick = {
                        //Open the drawer - its a suspend function remember
                        //TODO: side drawer
                    },
                    actions = listOf(
                        VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
                        VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
                        VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
                    )
                )
            }
        },
        floatingActionButton = floatingButton,
        contentWindowInsets = WindowInsets.safeDrawing
        ){ pd ->
        content(pd)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    VpTheme{
        HomeScreen(
            currentScreen = Screen.BottomScreen.Stories,
            title = "Voice",
            currentRoute = Screen.BottomScreen.Stories.bottomRoute,
            controller = rememberNavController()
        ) { pd ->
            Box(modifier = Modifier.padding(pd))
        }
    }
}
