package org.noormahal.vp25.android.presentation.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import org.noormahal.vp25.android.common.makeConnectionStatus
import org.noormahal.vp25.android.components.BroadcastGroupSummary
import org.noormahal.vp25.android.components.PersonProfile
import org.noormahal.vp25.android.presentation.viewmodel.ProfileViewModel

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
        isOwnProfile = uiState.isOwnProfile,
        onFollow = { viewModel.toggleFollowStatus() },
        onUnfollow = { viewModel.toggleFollowStatus() },
        onNickNameChange = { viewModel.updateUserNickname(it) }
    )
}
