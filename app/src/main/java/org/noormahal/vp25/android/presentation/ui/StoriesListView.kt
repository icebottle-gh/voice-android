package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.common.CURRENT_USER_USERNAME
import org.noormahal.vp25.android.components.FabStyle
import org.noormahal.vp25.android.components.StoriesList
import org.noormahal.vp25.android.components.VpFab
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel

/** Home-shell entry point for the Stories tab: owns its own chrome (title/FAB/actions). */
@Composable
fun StoriesListScreen(
    navController: NavHostController,
    storiesViewModel: StoriesViewModel,
    currentRoute: String?,
    onBottomScreenClick: (Screen) -> Unit
) {
    HomeShellScaffold(
        currentRoute = currentRoute,
        title = "VP25",
        titleColor = MaterialTheme.colorScheme.primary,
        titleFontWeight = FontWeight.SemiBold,
        onBottomScreenClick = onBottomScreenClick,
        actions = listOf(
            VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
            VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
            VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = { /*TODO*/ }),
        ),
        floatingActionButton = {
            VpFab(
                icon = ImageVector.vectorResource(id = R.drawable.baseline_create_24),
                contentDescription = "New Story",
                onClick = { /*TODO*/ },
                style = FabStyle.SQUIRCLE_PRIMARY
            )
        }
    ) { pd ->
        Box(modifier = Modifier.padding(pd)) {
            StoriesListView(navController, storiesViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesListView(navController: NavHostController, storiesViewModel: StoriesViewModel) {
    val usersList by storiesViewModel.usersList.collectAsState()

    // dummy data for now, pending real data wiring
//    val usersList = listOf(
//        User("sali", "Muhammed Salih", true),
//        User("hahi", "Ravi", true),
//        User("kiki", "Ahmed", false),
//        User("chuchu", "Dani", false),
//    )

    val myStory = usersList.find { it.userName == CURRENT_USER_USERNAME }

    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            storiesViewModel.refreshStories()
            pullRefreshState.endRefresh()
        }
    }

    // Refetch whenever this screen comes back to the foreground (app resumed, or
    // returning from another screen) - not just on the very first load, otherwise
    // stories posted since you last opened the app wouldn't show up until some
    // unrelated trigger (like a cold restart) happened to re-run the initial fetch.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch { storiesViewModel.refreshStories() }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        StoriesList(
            // hasUnviewedStory now carries its real meaning for your own row too -
            // whether you've actually opened your own story yet, same as everyone
            // else's. hasOwnStory (below) is the separate "do I have a story at all"
            // signal that decides Your Story vs Add Story.
            myStoryUser = myStory ?: User(CURRENT_USER_USERNAME, "Sajidha Abdulla", hasUnviewedStory = false),
            hasOwnStory = myStory != null,
            users = usersList.filterNot { it.userName == CURRENT_USER_USERNAME },
            onMyStoryClick = {
                if (myStory != null) {
                    navController.navigate(Screen.StoriesDetail.createRoute(CURRENT_USER_USERNAME))
                }
                // TODO: navigate to the new-story creation screen once it exists
            },
            onUserClick = { user -> navController.navigate(Screen.StoriesDetail.createRoute(user.userName)) }
        )

        PullToRefreshContainer(
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
