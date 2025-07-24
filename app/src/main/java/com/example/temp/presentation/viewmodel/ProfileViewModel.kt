package com.example.temp.presentation.viewmodel // Or your ViewModel package

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.temp.common.Client // Assuming your API client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.dto.PersonalizedProfile // Your DTO

// Data class to hold all profile screen state
data class ProfileScreenUiState(
    val profile: PersonalizedProfile? = null,
    val nickname: String? = null, // Store nickname separately if not part of PersonalizedProfile
    val isFollowing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOwnProfile: Boolean = false // To show/hide edit icon
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    // Replace with your actual logic to get the current logged-in user's ID
    private val currentLoggedInUserId = "currentUserStaticId" // Placeholder

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // --- Replace with your actual API calls ---
                // 1. Fetch user profile details
                // val fetchedProfile = apiClient.getUserProfile(userId) // Example
                val fetchedProfile = Client.user!!.people().getProfiles(listOf(userId)).get(0) // Example using existing search

                // 2. Fetch follow status
                // val followingStatus = apiClient.getFollowStatus(currentLoggedInUserId, userId)
                val followingStatus = Math.random() > 0.5 // Placeholder

                // 3. Fetch nickname (assuming it's stored separately or you have a way to get it)
                // val userNickname = apiClient.getNickname(userId)
                val userNickname = if (userId == "user123") "The Legend" else null // Placeholder

                if (fetchedProfile != null) {
                    _uiState.update {
                        it.copy(
                            profile = fetchedProfile,
                            nickname = userNickname, // Set fetched nickname
                            isFollowing = followingStatus,
                            isLoading = false,
                            isOwnProfile = userId == currentLoggedInUserId
                        )
                    }
                } else {
                    _uiState.update { it.copy(error = "User not found", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error fetching profile: ${e.message}", isLoading = false) }
                e.printStackTrace()
            }
        }
    }

    fun toggleFollowStatus() {
        val currentProfileId = _uiState.value.profile?.id ?: return
        val newFollowStatus = !_uiState.value.isFollowing
        viewModelScope.launch(Dispatchers.IO) {
            // --- Replace with your actual API call to follow/unfollow ---
            val connectionStatus = if (newFollowStatus) Client.user!!.connections().follow(currentProfileId) else Client.user!!.connections().unfollow(currentProfileId)
            // val success = apiClient.setFollowStatus(currentLoggedInUserId, currentProfileId, newFollowStatus)
            _uiState.update { it.copy(isFollowing = newFollowStatus) }
        }
    }

    fun updateUserNickname(newNickname: String?) {
        val profileId = _uiState.value.profile?.id ?: return
        viewModelScope.launch {
            // --- Replace with your actual API call to update nickname ---
            // val success = apiClient.updateNickname(profileId, newNickname)
            val success = true // Placeholder
            if (success) {
                _uiState.update { it.copy(nickname = newNickname?.takeIf { it.isNotBlank() }) }
            } else {
                // Handle error
            }
        }
    }
}
