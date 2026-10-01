package org.noormahal.vp25.android.presentation.viewmodel // Or your ViewModel package

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.common.Client // Assuming your API client
import org.noormahal.vp25.android.common.makePersonalizedProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.dto.AccountInformation
import org.noormahal.ib.vakkic.dto.PersonalizedProfile // Your DTO
import java.util.Calendar

private fun AccountInformation.toPersonalizedProfile(): PersonalizedProfile {
    val yearOfBirth = yearOfBirth?.toIntOrNull()
    val age = yearOfBirth?.let { (Calendar.getInstance().get(Calendar.YEAR) - it).toString() }
    return makePersonalizedProfile(
        id = username,
        fullName = fullName,
        nickName = null,
        bio = bio,
        age = age,
        gender = gender?.replaceFirstChar { it.uppercase() }
    )
}


data class ProfileScreenUiState(
    val profile: PersonalizedProfile? = null,
    val nickname: String? = null,
    val isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val connectionLoadFailed: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOwnProfile: Boolean = false
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    private val _actionError = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val actionError: SharedFlow<String> = _actionError.asSharedFlow()
    private var hasFetchedOwnProfile = false

    fun fetchOwnProfile() {
        if (hasFetchedOwnProfile) return
        hasFetchedOwnProfile = true
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val details = Client.user!!.account().getDetails()
                _uiState.update {
                    it.copy(
                        profile = details.toPersonalizedProfile(),
                        nickname = null,
                        isFollowing = false,
                        isLoading = false,
                        isOwnProfile = true
                    )
                }
            } catch (e: Exception) {
                hasFetchedOwnProfile = false
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
                val profileDeferred = async { Client.user!!.people().getProfiles(listOf(userId)).firstOrNull() }
                val ownUsernameDeferred = async { Client.user!!.account().getDetails().username }

                val connectionDeferred = async {
                    try {
                        Result.success(Client.user!!.connections().get().find { it.user == userId })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Client.reportIfUnauthorized(e)
                        Result.failure(e)
                    }
                }

                val fetchedProfile = profileDeferred.await()
                if (fetchedProfile != null) {
                    val ownUsername = ownUsernameDeferred.await()
                    val connectionResult = connectionDeferred.await()
                    val connectionStatus = connectionResult.getOrNull()
                    _uiState.update {
                        it.copy(
                            profile = fetchedProfile,
                            isFollowing = connectionStatus?.isFollowing ?: false,
                            isFollower = connectionStatus?.isFollower ?: false,
                            connectionLoadFailed = connectionResult.isFailure,
                            isLoading = false,
                            isOwnProfile = userId == ownUsername
                        )
                    }
                } else {
                    ownUsernameDeferred.cancel()
                    connectionDeferred.cancel()
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
                _actionError.tryEmit(e.message?.let { "Couldn't $action: $it" } ?: "Couldn't $action. Please try again.")
            }
        }
    }

    fun updateOwnBio(newBio: String?) {
        val currentProfile = _uiState.value.profile ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updated = Client.user!!.account().setDetails(currentProfile.fullName, newBio.orEmpty())
                _uiState.update { it.copy(profile = updated.toPersonalizedProfile()) }
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
                _actionError.tryEmit(e.message?.let { "Couldn't update bio: $it" } ?: "Couldn't update bio. Please try again.")
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
