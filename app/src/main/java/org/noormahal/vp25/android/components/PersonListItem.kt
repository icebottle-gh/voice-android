package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f, fill = false)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
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

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PersonListItemPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
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
            }
        }
    }
}
