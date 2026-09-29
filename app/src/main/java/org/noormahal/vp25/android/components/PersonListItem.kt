package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun PersonListItem(
    name: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    showAvatar: Boolean = false,
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showAvatar) {
            VpAvatar(size = 40.dp)
            Spacer(modifier = Modifier.width(12.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailingContent()
    }
}


@Composable
fun PersonListItemSkeleton(
    modifier: Modifier = Modifier,
    showSubtitle: Boolean = false,
    showTrailing: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerCircle(size = 40.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            ShimmerLine(widthFraction = 0.5f)
            if (showSubtitle) {
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerLine(widthFraction = 0.3f, height = 12.dp)
            }
        }
        if (showTrailing) {
            Spacer(modifier = Modifier.width(8.dp))
            ShimmerBox(width = 72.dp, height = 32.dp, shape = RoundedCornerShape(50))
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PersonListItemPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                // With leading avatar
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Follows you",
                    showAvatar = true,
                    trailingContent = {
                        FollowStatusButton(status = FollowStatus.FOLLOW, onFollow = {}, onUnfollow = {})
                    }
                )

                // People & Contacts
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Follows you",
                    trailingContent = {
                        FollowStatusButton(status = FollowStatus.FOLLOW, onFollow = {}, onUnfollow = {})
                    }
                )
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Follows you",
                    trailingContent = {
                        FollowStatusButton(status = FollowStatus.FOLLOWING, onFollow = {}, onUnfollow = {})
                    }
                )
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Follows you",
                    trailingContent = {
                        FollowStatusButton(status = FollowStatus.REQUESTED, onFollow = {}, onUnfollow = {}, onCancelRequest = {})
                    }
                )

                // Contacts
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Phone Number",
                    trailingContent = {
                        VpButton(label = { Text("Invite") }, onClick = {}, style = ButtonStyle.PRIMARY_FRAMELESS)
                    }
                )
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Phone Number",
                    trailingContent = {
                        VpButton(label = { Text("Invited") }, onClick = {}, style = ButtonStyle.PRIMARY_FRAMELESS, enabled = false)
                    }
                )

                // Selectable
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Text",
                    onClick = {},
                    trailingContent = {
                        VpRadioButton(selected = false, onClick = {})
                    }
                )
                PersonListItem(
                    name = "Person Name",
                    subtitle = "Text",
                    onClick = {},
                    trailingContent = {
                        VpRadioButton(selected = true, onClick = {})
                    }
                )

                // Group Members — no notes given for this variant's trailing control;
                // assumed to be a plain row with no action until specified otherwise.
                PersonListItem(name = "Person Name")

                // Follow Requests
                PersonListItem(
                    name = "Person Name",
                    trailingContent = {
                        Row {
                            VpButton(label = { Text("Accept") }, onClick = {}, style = ButtonStyle.ROUND_PRIMARY)
                            Spacer(modifier = Modifier.width(8.dp))
                            VpButton(label = { Text("Decline") }, onClick = {}, style = ButtonStyle.ROUND_SECONDARY)
                        }
                    }
                )

                // Skeleton (loading) — name only, e.g. Find
                PersonListItemSkeleton()
                PersonListItemSkeleton()

                // Skeleton (loading) — name + subtitle + trailing action
                PersonListItemSkeleton(showSubtitle = true, showTrailing = true)
                PersonListItemSkeleton(showSubtitle = true, showTrailing = true)
            }
        }
    }
}
