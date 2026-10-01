package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import org.noormahal.vp25.android.R
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

@Composable
fun StoriesListView(navController: NavHostController, storiesViewModel: StoriesViewModel) {
//    val usersList = storiesViewModel.usersList.collectAsState(initial = listOf())

    // dummy data for now, pending real data wiring
    val usersList = listOf(
        User("Saji", "Sajidha Abdulla", true),
        User("sali", "Muhammed Salih", true),
        User("hahi", "Ravi", true),
        User("kiki", "Ahmed", false),
        User("chuchu", "Dani", false),
    )

    StoriesList(
        myStoryUser = User("123", "Sajidha", hasUnviewedStory = false),
        users = usersList,
        onMyStoryClick = { /* navigate to my stories detail */ },
        onUserClick = { user -> navController.navigate(Screen.StoriesDetail.createRoute(user.userName)) }
    )
}
//    val usersList = storiesViewModel.getStoryListUsers
