package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.noormahal.vp25.android.common.makeConnectionStatus
import org.noormahal.vp25.android.common.makePersonalizedProfile
import org.noormahal.ib.vakkic.dto.ConnectionStatus
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

@Composable
fun PersonProfile(
    isLoading: Boolean,
    error: String?,
    profile: PersonalizedProfile?,
    connectionStatus: ConnectionStatus?,
    isOwnProfile: Boolean,
    onFollow: () -> Unit,
    onUnfollow: () -> Unit,
    onNickNameChange: (String?) -> Unit
) {
    var showEditNicknameDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text("Error: ${error}", color = MaterialTheme.colorScheme.error)
            }
            profile != null -> {
                ProfileContent(
                    profile = profile,
                    connectionStatus = connectionStatus,
                    isOwnProfile = isOwnProfile,
                    modifier = Modifier.fillMaxSize(),
                    onFollow = onFollow,
                    onUnfollow = onUnfollow,
                    onEditNicknameClick = { showEditNicknameDialog = true }
                )
            }
            else -> {
                Text("Profile not found or still loading.")
            }
        }
    }

    if (showEditNicknameDialog && profile != null) {
        EditNicknameDialog(
            currentNickname = profile.nickName ?: profile.fullName, // Or fullName
            onDismiss = { showEditNicknameDialog = false },
            onConfirm = { newNickname ->
                onNickNameChange(newNickname)
                showEditNicknameDialog = false
            }
        )
    }
}

@Composable
fun ProfileContent(
    profile: PersonalizedProfile,
    isOwnProfile: Boolean,
    connectionStatus: ConnectionStatus?,
    modifier: Modifier,
    onFollow: () -> Unit,
    onUnfollow: () -> Unit,
    onEditNicknameClick: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()) // Make content scrollable
            .padding(16.dp),
    ) {

        // FullName or Nickname with Edit Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            val displayName = if (profile.nickName.isNullOrBlank()) {
                profile.fullName // Or profile.fullName
            } else {
                "[ ${profile.nickName} ]"
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            if (!isOwnProfile) { // Show edit icon for other's profile
                IconButton(onClick = onEditNicknameClick, modifier = Modifier.size(36.dp)) { // Slightly larger touch target
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Nickname",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bio
        Text(
            text = profile.bio ?: "No bio available.", // Assuming 'bio' property in PersonalizedProfile
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Follow/Unfollow Button (conditionally shown if not own profile)
        if (!isOwnProfile && connectionStatus != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = if (connectionStatus.isFollowing) onUnfollow else onFollow,
                    modifier = Modifier
                        .fillMaxWidth(0.6f) // Takes 60% of width
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (connectionStatus.isFollowing) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.primary,
                        contentColor = if (connectionStatus.isFollowing) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = if (connectionStatus.isFollowing) "Unfollow" else "Follow",
                        fontSize = 16.sp
                    )
                }
                if (connectionStatus.isFollower) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Follows you"
                    )
                }
            }
        }
    }
}

@Composable
fun EditNicknameDialog(
    currentNickname: String?,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {
    var nicknameInput by remember { mutableStateOf(currentNickname ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Nickname") },
        text = {
            OutlinedTextField(
                value = nicknameInput,
                onValueChange = { nicknameInput = it },
                label = { Text("Nickname (optional)") },
                placeholder = { Text("Enter nickname or leave blank") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(nicknameInput.takeIf { it.isNotBlank() }) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PersonProfilePreview() {
    PersonProfile(
        isLoading = false,
        error = null,
        profile = makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = "Johny", bio = "I develop android apps using kotlin and jetpack compose"),
        connectionStatus = makeConnectionStatus(user = "1", isFollowing = true, isFollower = true),
        isOwnProfile = false,
        onFollow = {},
        onUnfollow = {},
        onNickNameChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PersonProfileLongBioPreview() {
    PersonProfile(
        isLoading = false,
        error = null,
        profile = makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = null,
            bio = "John Doe graduated from the University of Georgia with a degree in history in 1980. After " +
                    "graduating, he spent 35 years in a career focused in the public policy arena and in consulting for " +
                    "Fortune 100 clients. Since retiring from that world, he’s combined his business experience with " +
                    "his liberal arts foundation by building and incubating several successful start-up businesses",
        ),
        connectionStatus = makeConnectionStatus(user = "1", isFollowing = false, isFollower = false),
        isOwnProfile = false,
        onFollow = {},
        onUnfollow = {},
        onNickNameChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun OwnPersonProfilePreview() {
    PersonProfile(
        isLoading = false,
        error = null,
        profile = makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = null, bio = "I develop android apps"),
        connectionStatus = null,
        isOwnProfile = true,
        onFollow = {},
        onUnfollow = {},
        onNickNameChange = {}
    )
}