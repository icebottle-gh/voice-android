package org.noormahal.vp25.android.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.common.makeConnectionStatus
import org.noormahal.vp25.android.components.BroadcastGroupSummary
import org.noormahal.vp25.android.components.PersonProfile
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.viewmodel.ProfileViewModel


@Composable
fun OwnProfileScreen(
    currentRoute: String?,
    onBottomScreenClick: (Screen) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    HomeShellScaffold(
        currentRoute = currentRoute,
        title = "",
        onBottomScreenClick = onBottomScreenClick,
        actions = listOf(
            VpTopAppBarAction(
                icon = ImageVector.vectorResource(id = R.drawable.outline_logout_24),
                label = "Logout",
                onClick = onLogout
            )
        )
    ) { pd ->
        Box(modifier = Modifier.padding(pd)) {
            OwnProfileView(viewModel)
        }
    }
}

@Composable
fun PersonProfileScreen(
    userId: String,
    currentRoute: String?,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    HomeShellScaffold(
        currentRoute = currentRoute,
        title = "",
        showBackButton = true,
        onBack = onBack,
        showBottomBar = false
    ) { pd ->
        Box(modifier = Modifier.padding(pd)) {
            PersonProfileView(userId = userId, viewModel = viewModel)
        }
    }
}

@Composable
fun OwnProfileView(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchOwnProfile()
    }

    LaunchedEffect(Unit) {
        viewModel.actionError.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    PersonProfile(
        isLoading = uiState.isLoading,
        error = uiState.error,
        profile = uiState.profile,
        connectionStatus = null,
        isOwnProfile = true,
        broadcastsList = listOf(
            BroadcastGroupSummary(name = "Followers", recipientsCount = 85, isPrivate = true),
            BroadcastGroupSummary(name = "Family", recipientsCount = 10, isPrivate = true)
        ),
        onFollow = {},
        onUnfollow = {},
        onNickNameChange = { viewModel.updateUserNickname(it) },
        onBioChange = { viewModel.updateOwnBio(it) }
    )
}

@Composable
fun PersonProfileView(userId: String, viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        viewModel.fetchUserProfile(userId)
    }

    LaunchedEffect(Unit) {
        viewModel.actionError.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    PersonProfile(
        isLoading = uiState.isLoading,
        error = uiState.error,
        profile = uiState.profile,
        connectionStatus = uiState.profile?.let {
            makeConnectionStatus(user = it.id, isFollowing = uiState.isFollowing, isFollower = uiState.isFollower)
        },
        connectionLoadFailed = uiState.connectionLoadFailed,
        isOwnProfile = uiState.isOwnProfile,
        onFollow = { viewModel.toggleFollowStatus() },
        onUnfollow = { viewModel.toggleFollowStatus() },
        onNickNameChange = { viewModel.updateUserNickname(it) }
    )
}
