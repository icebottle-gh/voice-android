package org.noormahal.vp25.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.noormahal.vp25.android.common.makeConnectionStatus
import org.noormahal.vp25.android.components.BroadcastGroupSummary
import org.noormahal.vp25.android.components.PersonProfile
import org.noormahal.vp25.android.presentation.viewmodel.ProfileViewModel

@Composable
fun OwnProfileView(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchOwnProfile()
    }

    // TODO: ProfileViewModel doesn't fetch the user's broadcast groups yet - placeholder
    //  data until that's wired up.
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
        onNickNameChange = { viewModel.updateUserNickname(it) }
    )
}

@Composable
fun PersonProfileView(userId: String, viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchUserProfile(userId)
    }

    PersonProfile(
        isLoading = uiState.isLoading,
        error = uiState.error,
        profile = uiState.profile,
        connectionStatus = uiState.profile?.let {
            // TODO: isFollower isn't tracked by ProfileViewModel yet
            makeConnectionStatus(user = it.id, isFollowing = uiState.isFollowing, isFollower = false)
        },
        isOwnProfile = uiState.isOwnProfile,
        onFollow = { viewModel.toggleFollowStatus() },
        onUnfollow = { viewModel.toggleFollowStatus() },
        onNickNameChange = { viewModel.updateUserNickname(it) }
    )
}
