package org.noormahal.vp25.android.components

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.common.makeConnectionStatus
import org.noormahal.vp25.android.common.makePersonalizedProfile
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme
import org.noormahal.ib.vakkic.dto.ConnectionStatus
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

@Composable
fun PersonProfile(
    isLoading: Boolean,
    error: String?,
    profile: PersonalizedProfile?,
    connectionStatus: ConnectionStatus?,
    isOwnProfile: Boolean,
    broadcastsList: List<BroadcastGroupSummary> = emptyList(),
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
                    broadcastsList = broadcastsList,
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
    broadcastsList: List<BroadcastGroupSummary>,
    modifier: Modifier,
    onFollow: () -> Unit,
    onUnfollow: () -> Unit,
    onEditNicknameClick: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            VpAvatar()

            Spacer(modifier = Modifier.height(4.dp))

            // Nickname (a private label you give to someone else - not applicable to your own profile)
            if (!isOwnProfile) {
                if (!profile.nickName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = VpSpacing.screenHorizontal)
                    ) {
                        Text(
                            text = "[ ${profile.nickName} ]",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        IconButton(
                            onClick = onEditNicknameClick,
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Nickname",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onEditNicknameClick)
                            .padding(horizontal = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nickname",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Nickname",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            // TODO: age/gender aren't populated by the profile API yet - wire these to
            //  profile.age / profile.gender once the backend fills them in.
            Text(
                text = profile.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Age: 25, Gender: Female",
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Follow/Following/Requested Button (other people's profiles only)
            if (!isOwnProfile && connectionStatus != null) {
                // TODO: ConnectionStatus only carries isFollowing/isFollower today, so REQUESTED
                //  can't be derived yet - wire that up once the "requested" state exists in the data layer.
                val followStatus = if (connectionStatus.isFollowing) FollowStatus.FOLLOWING else FollowStatus.FOLLOW
                FollowStatusButton(
                    status = followStatus,
                    onFollow = onFollow,
                    onUnfollow = onUnfollow,
                    modifier = Modifier.fillMaxWidth(0.4f)
                )
                if (connectionStatus.isFollower) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Follows you",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Bio
        Column(
            modifier = Modifier.padding(horizontal = VpSpacing.screenHorizontal)
        ) {
            if (!profile.bio.isNullOrBlank()) {
                ExpandableBioInline(bio = profile.bio)
            } else if (isOwnProfile) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { /* TODO: Open bio edit */ }
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Add Bio",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add Bio",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                ExpandableBioInline(bio = "No Bio Available")
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Groups (own profile only)
        if (isOwnProfile) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = VpSpacing.screenHorizontal),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Groups",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { /* TODO: create new group */ }
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add New Group",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "New Group",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Content above is already inside a verticalScroll Column, so the group
                // list is rendered directly rather than via LazyColumn (nesting a lazy
                // scrollable inside a scrollable Column of the same orientation crashes).
                broadcastsList.forEach { group ->
                    BroadcastGroupListItem(group = group)
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
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun PersonProfileLongBioPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun OwnPersonProfilePreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PersonProfile(
                isLoading = false,
                error = null,
                profile = makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = null, bio = "I develop android apps"),
                connectionStatus = null,
                isOwnProfile = true,
                broadcastsList = listOf(
                    BroadcastGroupSummary(name = "Followers", recipientsCount = 85, isPrivate = true),
                    BroadcastGroupSummary(name = "Family", recipientsCount = 10, isPrivate = true)
                ),
                onFollow = {},
                onUnfollow = {},
                onNickNameChange = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OwnPersonProfileNoBioPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PersonProfile(
                isLoading = false,
                error = null,
                profile = makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = null, bio = ""),
                connectionStatus = null,
                isOwnProfile = true,
                broadcastsList = emptyList(),
                onFollow = {},
                onUnfollow = {},
                onNickNameChange = {}
            )
        }
    }
}
