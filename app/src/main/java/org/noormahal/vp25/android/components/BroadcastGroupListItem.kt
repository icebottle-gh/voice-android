package org.noormahal.vp25.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

data class BroadcastGroupSummary(
    val name: String,
    val recipientsCount: Int,
    val isPrivate: Boolean = true
)

@Composable
fun BroadcastGroupListItem(
    group: BroadcastGroupSummary,
    onClick: () -> Unit = {},
    onAddStoryClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = VpSpacing.screenHorizontal),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .weight(1f, fill = false)
            ) {
                Row {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    VpTag(text = if (group.isPrivate) "Private" else "Public")
                }

                Text(
                    text = "${group.recipientsCount} recipients",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onAddStoryClick,
                modifier = Modifier.size(22.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_post_add_24),
                    contentDescription = "Add Story",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BroadcastGroupListItemPreview() {
    VpTheme{
        BroadcastGroupListItem(
            group = BroadcastGroupSummary(name = "Group Name", recipientsCount = 85, isPrivate = true)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BroadcastGroupListItemPublicPreview() {
    VpTheme{
        BroadcastGroupListItem(
            group = BroadcastGroupSummary(name = "Family", recipientsCount = 10, isPrivate = false)
        )
    }
}
