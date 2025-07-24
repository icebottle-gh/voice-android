package com.example.temp.presentation.ui // Or your UI package

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource // For placeholder image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.temp.R // Assuming you have a placeholder drawable
import com.example.temp.presentation.viewmodel.ProfileScreenUiState
import com.example.temp.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String?,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()
    var showEditNicknameDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId != null && userId.isNotBlank()) {
            profileViewModel.fetchUserProfile(userId)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
            uiState.profile != null -> {
                ProfileContent(
                    uiState = uiState,
                    modifier = Modifier.fillMaxSize(),
                    onFollowToggle = { profileViewModel.toggleFollowStatus() },
                    onEditNicknameClick = { showEditNicknameDialog = true }
                )
            }
            userId == null -> {
                Text("No User ID provided.")
            }
            else -> {
                Text("Profile not found or still loading.")
            }
        }
    }

    if (showEditNicknameDialog) {
        EditNicknameDialog(
            currentNickname = uiState.nickname ?: uiState.profile?.fullName, // Or fullName
            onDismiss = { showEditNicknameDialog = false },
            onConfirm = { newNickname ->
                profileViewModel.updateUserNickname(newNickname)
                showEditNicknameDialog = false
            }
        )
    }
}

@Composable
fun ProfileContent(
    uiState: ProfileScreenUiState,
    modifier: Modifier,
    onFollowToggle: () -> Unit,
    onEditNicknameClick: () -> Unit
) {
    val profile = uiState.profile ?: return // Should not happen if called correctly

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()) // Make content scrollable
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture Placeholder
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with actual image loading (Coil, Glide)
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // FullName or Nickname with Edit Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val displayName = if (uiState.nickname.isNullOrBlank()) {
                profile.fullName // Or profile.fullName
            } else {
                "[ ${uiState.nickname} ]"
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            if (!uiState.isOwnProfile) { // Show edit icon only for others profile
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

        // Username (Optional, if you have it)
        // Text(
        //     text = "@${profile.username}", // Assuming a username field
        //     style = MaterialTheme.typography.bodyMedium,
        //     color = MaterialTheme.colorScheme.onSurfaceVariant
        // )

        Spacer(modifier = Modifier.height(16.dp))

        // Bio
        Text(
            text = profile.bio ?: "No bio available.", // Assuming 'bio' property in PersonalizedProfile
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Follow/Unfollow Button (conditionally shown if not own profile)
        if (!uiState.isOwnProfile) {
            Button(
                onClick = onFollowToggle,
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Takes 60% of width
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isFollowing) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    contentColor = if (uiState.isFollowing) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (uiState.isFollowing) "Unfollow" else "Follow",
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder for other content like posts, stats, etc.
        // Divider(modifier = Modifier.padding(vertical = 16.dp))
        // Text("User's Activity / Posts", style = MaterialTheme.typography.titleMedium)
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
