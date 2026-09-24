package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.components.FabStyle
import org.noormahal.vp25.android.components.VpBottomBar
import org.noormahal.vp25.android.components.VpDrawerItem
import org.noormahal.vp25.android.components.VpFab
import org.noormahal.vp25.android.components.VpNavigationDrawer
import org.noormahal.vp25.android.components.VpTopAppBar
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.presentation.navigation.HomeNavGraph
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.navigation.allScreens
import org.noormahal.vp25.android.presentation.navigation.screensWithBottom
import org.noormahal.vp25.android.presentation.navigation.screensWithTopBar
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel
import org.noormahal.vp25.android.presentation.viewmodel.ProfileViewModel
import org.noormahal.vp25.android.theme.VpTheme


@Composable
fun HomeView(
    loginViewModel: LoginViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()

    val profileUiState by profileViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        profileViewModel.fetchOwnProfile()
    }


    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen = remember(currentRoute) {
        allScreens.find { it.route == currentRoute } ?: Screen.BottomScreen.Stories
    }

    VpNavigationDrawer(
        drawerState = drawerState,
        items = listOf(
            VpDrawerItem(label = "Logout", onClick = { loginViewModel.logout() })
        ),
        userName = profileUiState.profile?.fullName
    ) {
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
            },
            onOpenDrawer = { drawerScope.launch { drawerState.open() } },
            onBack = { controller.popBackStack() }
        ) { pd ->
            HomeNavGraph(navController = controller, pd = pd)
        }
    }
}

@Composable
private fun HomeScreen(
    currentScreen: Screen,
    currentRoute: String?,
    onBottomScreenClick: (Screen) -> Unit,
    onOpenDrawer: () -> Unit = {},
    onBack: () -> Unit = {},
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
            if (currentScreen in screensWithTopBar) {
                val isPersonProfile = currentScreen == Screen.PersonProfile
                VpTopAppBar(
                    title = title,
                    showLeadingIcon = currentScreen == Screen.BottomScreen.Stories || isPersonProfile,
                    leadingIcon = if (isPersonProfile) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.AccountCircle,
                    leadingIconContentDescription = if (isPersonProfile) "Back" else "Menu",
                    onLeadingIconClick = if (isPersonProfile) onBack else onOpenDrawer,
                    actions = if (isPersonProfile) emptyList() else listOf(
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
