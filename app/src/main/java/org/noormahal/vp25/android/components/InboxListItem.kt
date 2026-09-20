package org.noormahal.vp25.android.components

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

enum class InboxItemType { INCOMING, OUTGOING }

data class InboxItemSummary(
    val personName: String,
    val lastMessage: String,
    val time: String,
    val type: InboxItemType,
    val unreadCount: Int = 0,
    val messageStatus: MessageStatus = MessageStatus.SENT
)

@Composable
fun InboxListItem(
    item: InboxItemSummary,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = VpSpacing.screenHorizontal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f, fill = false)) {
                Text(
                    text = item.personName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.type == InboxItemType.OUTGOING) {
                        MessageStatusIndicator(status = item.messageStatus)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = item.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.height(20.dp)) {
                    if (item.type == InboxItemType.INCOMING) {
                        VpBadge(type = VpBadgeType.Number, number = item.unreadCount)
                    }
                }
            }
        }
    }
}



@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InboxListItemPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                InboxListItem(
                    item = InboxItemSummary(
                        personName = "Person Name",
                        lastMessage = "Last message",
                        time = "Time",
                        type = InboxItemType.INCOMING,
                        unreadCount = 36
                    )
                )
                InboxListItem(
                    item = InboxItemSummary(
                        personName = "Person Name",
                        lastMessage = "Last message",
                        time = "Time",
                        type = InboxItemType.OUTGOING,
                        messageStatus = MessageStatus.SENDING
                    )
                )
                InboxListItem(
                    item = InboxItemSummary(
                        personName = "Person Name",
                        lastMessage = "Last message",
                        time = "Time",
                        type = InboxItemType.OUTGOING,
                        messageStatus = MessageStatus.SENT
                    )
                )
                InboxListItem(
                    item = InboxItemSummary(
                        personName = "Person Name",
                        lastMessage = "Last message",
                        time = "Time",
                        type = InboxItemType.OUTGOING,
                        messageStatus = MessageStatus.RECEIVED
                    )
                )
                InboxListItem(
                    item = InboxItemSummary(
                        personName = "Person Name",
                        lastMessage = "Last message",
                        time = "Time",
                        type = InboxItemType.OUTGOING,
                        messageStatus = MessageStatus.ERROR
                    )
                )
            }
        }
    }
}
