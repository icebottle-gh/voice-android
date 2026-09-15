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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.components.FabStyle
import org.noormahal.vp25.android.components.VpBottomBar
import org.noormahal.vp25.android.components.VpFab
import org.noormahal.vp25.android.components.VpTopAppBar
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.presentation.navigation.HomeNavGraph
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.navigation.allScreens
import org.noormahal.vp25.android.presentation.navigation.screensWithBottom
import org.noormahal.vp25.android.theme.VpTheme


@Composable
fun HomeView() {

//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Allow us to find out on which view we currently are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // currentScreen is fully derived from currentRoute - the NavController back stack
    // is the single source of truth, so this never needs manual syncing.
    val currentScreen = remember(currentRoute) {
        allScreens.find { it.route == currentRoute } ?: Screen.BottomScreen.Stories
    }

    HomeScreen(
        currentScreen = currentScreen,
        currentRoute = currentRoute,
        onBottomScreenClick = { screen ->
            controller.navigate(screen.route) {
                popUpTo(controller.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) { pd ->
        HomeNavGraph(navController = controller, pd = pd)
    }
}

@Composable
private fun HomeScreen(
    currentScreen: Screen,
    currentRoute: String?,
    onBottomScreenClick: (Screen) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val title = if (currentScreen == Screen.BottomScreen.Stories) "Voice" else currentScreen.title

    val floatingButton: @Composable () -> Unit = {
        if (currentScreen == Screen.BottomScreen.Stories){
            VpFab(
                icon = ImageVector.vectorResource(id = R.drawable.baseline_create_24),
                contentDescription = "New Story",
                onClick = { /*TODO*/ },
                style = FabStyle.SQUIRCLE_PRIMARY
            )
        }
        else if (currentScreen == Screen.BottomScreen.Chats){
            VpFab(
                icon = ImageVector.vectorResource(id = R.drawable.baseline_message_24),
                contentDescription = "New Message",
                onClick = { /*TODO*/ },
                style = FabStyle.SQUIRCLE_PRIMARY
            )
        }
    }

    Scaffold(
//        modifier = Modifier.safeDrawingPadding(),
        bottomBar = {
            VpBottomBar(
                currentScreen = currentScreen,
//                viewModel = viewModel,
                currentRoute = currentRoute,
                items = screensWithBottom,
                onBottomScreenClick = onBottomScreenClick
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
            currentRoute = Screen.BottomScreen.Stories.bottomRoute,
            onBottomScreenClick = {}
        ) { pd ->
            Box(modifier = Modifier.padding(pd))
        }
    }
}
