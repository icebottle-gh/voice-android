package org.noormahal.vp25.android.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

enum class FollowStatus { FOLLOW, FOLLOWING, REQUESTED }

@Composable
fun FollowStatusButton(
    status: FollowStatus,
    onFollow: () -> Unit,
    onUnfollow: () -> Unit,
    onCancelRequest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    val label = when (status) {
        FollowStatus.FOLLOW -> "Follow"
        FollowStatus.FOLLOWING -> "Following"
        FollowStatus.REQUESTED -> "Requested"
    }
    val style = if (status == FollowStatus.FOLLOW) ButtonStyle.ROUND_PRIMARY else ButtonStyle.ROUND_SECONDARY

    VpButton(
        label = { Text(label) },
        onClick = {
            when (status) {
                FollowStatus.FOLLOW -> onFollow()
                FollowStatus.FOLLOWING, FollowStatus.REQUESTED -> showConfirmDialog = true
            }
        },
        modifier = modifier,
        style = style
    )

    if (showConfirmDialog) {
        val isFollowing = status == FollowStatus.FOLLOWING
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(if (isFollowing) "Unfollow?" else "Cancel request?") },
            text = {
                Text(
                    if (isFollowing) "Are you sure you want to unfollow this person?"
                    else "Are you sure you want to cancel your follow request?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    if (isFollowing) onUnfollow() else onCancelRequest()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No")
                }
            },
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FollowStatusButtonFollowPreview() {
    VpTheme{
        FollowStatusButton(status = FollowStatus.FOLLOW, onFollow = {}, onUnfollow = {})
    }
}

@Preview(showBackground = true)
@Composable
fun FollowStatusButtonFollowingPreview() {
    VpTheme{
        FollowStatusButton(status = FollowStatus.FOLLOWING, onFollow = {}, onUnfollow = {})
    }
}

@Preview(showBackground = true)
@Composable
fun FollowStatusButtonRequestedPreview() {
    VpTheme{
        FollowStatusButton(status = FollowStatus.REQUESTED, onFollow = {}, onUnfollow = {}, onCancelRequest = {})
    }
}
