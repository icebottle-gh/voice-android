package org.noormahal.vp25.android.presentation.viewmodel // Or your ViewModel package

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.common.Client // Assuming your API client
import org.noormahal.vp25.android.common.makePersonalizedProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.dto.PersonalizedProfile // Your DTO
import java.util.Calendar

// Data class to hold all profile screen state
data class ProfileScreenUiState(
    val profile: PersonalizedProfile? = null,
    val nickname: String? = null, // Store nickname separately if not part of PersonalizedProfile
    val isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOwnProfile: Boolean = false // To show/hide edit icon
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    private val _followActionError = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val followActionError: SharedFlow<String> = _followActionError.asSharedFlow()

    fun fetchOwnProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val details = Client.user!!.account().getDetails()
                val yearOfBirth = details.yearOfBirth?.toIntOrNull()
                val age = yearOfBirth?.let { (Calendar.getInstance().get(Calendar.YEAR) - it).toString() }
                _uiState.update {
                    it.copy(
                        profile = makePersonalizedProfile(
                            id = details.username,
                            fullName = details.fullName,
                            nickName = null,
                            bio = details.bio,
                            age = age,
                            gender = details.gender?.replaceFirstChar { it.uppercase() }
                        ),
                        nickname = null,
                        isFollowing = false,
                        isLoading = false,
                        isOwnProfile = true
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
                _uiState.update { it.copy(error = e.message ?: "Something went wrong. Please try again.", isLoading = false) }
            }
        }
    }

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val fetchedProfile = Client.user!!.people().getProfiles(listOf(userId)).firstOrNull()
                if (fetchedProfile != null) {
                    val ownUsername = Client.user!!.account().getDetails().username
                    // Connection status is best-effort: a failure here (e.g. the connections
                    // endpoint erroring out) shouldn't block the rest of the profile from showing.
                    val connectionStatus = try {
                        Client.user!!.connections().get().find { it.user == userId }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Client.reportIfUnauthorized(e)
                        null
                    }
                    _uiState.update {
                        it.copy(
                            profile = fetchedProfile,
                            isFollowing = connectionStatus?.isFollowing ?: false,
                            isFollower = connectionStatus?.isFollower ?: false,
                            isLoading = false,
                            isOwnProfile = userId == ownUsername
                        )
                    }
                } else {
                    _uiState.update { it.copy(error = "User not found", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error fetching profile: ${e.message}", isLoading = false) }
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
            }
        }
    }

    fun toggleFollowStatus() {
        val currentProfileId = _uiState.value.profile?.id ?: return
        val newFollowStatus = !_uiState.value.isFollowing
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // --- Replace with your actual API call to follow/unfollow ---
                if (newFollowStatus) Client.user!!.connections().follow(currentProfileId) else Client.user!!.connections().unfollow(currentProfileId)
                // val success = apiClient.setFollowStatus(currentLoggedInUserId, currentProfileId, newFollowStatus)
                _uiState.update { it.copy(isFollowing = newFollowStatus) }
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
                val action = if (newFollowStatus) "follow" else "unfollow"
                _followActionError.tryEmit(e.message?.let { "Couldn't $action: $it" } ?: "Couldn't $action. Please try again.")
            }
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
