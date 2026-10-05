package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
        VpDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = if (isFollowing) "Unfollow?" else "Cancel request?",
            description = if (isFollowing) "Are you sure you want to unfollow this person?"
                else "Are you sure you want to cancel your follow request?",
            buttons = listOf(
                VpDialogButtonSpec(
                    text = "No",
                    onClick = { showConfirmDialog = false },
                    style = ButtonStyle.ROUND_PRIMARY_OUTLINE
                ),
                VpDialogButtonSpec(
                    text = "Yes",
                    onClick = {
                        showConfirmDialog = false
                        if (isFollowing) onUnfollow() else onCancelRequest()
                    },
                    style = ButtonStyle.ROUND_PRIMARY
                )
            )
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

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UnfollowConfirmDialogPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VpDialogCard(
                title = "Unfollow?",
                description = "Are you sure you want to unfollow this person?",
                buttons = listOf(
                    VpDialogButtonSpec(text = "No", onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE),
                    VpDialogButtonSpec(text = "Yes", onClick = {}, style = ButtonStyle.ROUND_PRIMARY)
                )
            )
        }
    }
}
