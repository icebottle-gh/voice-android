package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

enum class ChatBubbleType { INCOMING, OUTGOING }
enum class ChatBubbleOrder { FIRST, MIDDLE, LAST }

data class ChatBubbleSummary(
    val type: ChatBubbleType,
    val order: ChatBubbleOrder,
    val message: String,
    val timestamp: String,
    val messageStatus: MessageStatus = MessageStatus.SENT,
    val showName: Boolean = false,
    val name: String = ""
)


private val BUBBLE_CORNER_LARGE = 16.dp
private val BUBBLE_CORNER_SMALL = 4.dp

@Composable
fun ChatBubble(
    item: ChatBubbleSummary,
    modifier: Modifier = Modifier
) {
    val isOutgoing = item.type == ChatBubbleType.OUTGOING

    val (nearTopCorner, nearBottomCorner) = when (item.order) {
        ChatBubbleOrder.FIRST -> BUBBLE_CORNER_LARGE to BUBBLE_CORNER_SMALL
        ChatBubbleOrder.MIDDLE -> BUBBLE_CORNER_SMALL to BUBBLE_CORNER_SMALL
        ChatBubbleOrder.LAST -> BUBBLE_CORNER_SMALL to BUBBLE_CORNER_LARGE
    }

    val shape = if (isOutgoing) {
        RoundedCornerShape(
            topStart = BUBBLE_CORNER_LARGE,
            topEnd = nearTopCorner,
            bottomEnd = nearBottomCorner,
            bottomStart = BUBBLE_CORNER_LARGE
        )
    } else {
        RoundedCornerShape(
            topStart = nearTopCorner,
            topEnd = BUBBLE_CORNER_LARGE,
            bottomEnd = BUBBLE_CORNER_LARGE,
            bottomStart = nearBottomCorner
        )
    }

    val containerColor = if (isOutgoing) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (isOutgoing) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (item.showName) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
                if (isOutgoing) {
                    Spacer(modifier = Modifier.width(4.dp))
                    MessageStatusIndicator(status = item.messageStatus)
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChatBubbleGroupPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    ChatBubble(
                        item = ChatBubbleSummary(
                            type = ChatBubbleType.OUTGOING,
                            order = ChatBubbleOrder.FIRST,
                            message = "Message. Lorem ipsum dolor sit amet",
                            timestamp = "09:45",
                            messageStatus = MessageStatus.SENT
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    ChatBubble(
                        item = ChatBubbleSummary(
                            type = ChatBubbleType.OUTGOING,
                            order = ChatBubbleOrder.MIDDLE,
                            message = "Message. Lorem ipsum dolor sit amet",
                            timestamp = "09:45",
                            messageStatus = MessageStatus.SENT
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    ChatBubble(
                        item = ChatBubbleSummary(
                            type = ChatBubbleType.OUTGOING,
                            order = ChatBubbleOrder.LAST,
                            message = "Message. Lorem ipsum dolor sit amet",
                            timestamp = "09:45",
                            messageStatus = MessageStatus.RECEIVED
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.Start) {
                    ChatBubble(
                        item = ChatBubbleSummary(
                            type = ChatBubbleType.INCOMING,
                            order = ChatBubbleOrder.FIRST,
                            message = "Message. Lorem ipsum dolor sit amet",
                            timestamp = "09:45",
                            showName = true,
                            name = "Name"
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    ChatBubble(
                        item = ChatBubbleSummary(
                            type = ChatBubbleType.INCOMING,
                            order = ChatBubbleOrder.MIDDLE,
                            message = "Message. Lorem ipsum dolor sit amet",
                            timestamp = "09:45"
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    ChatBubble(
                        item = ChatBubbleSummary(
                            type = ChatBubbleType.INCOMING,
                            order = ChatBubbleOrder.LAST,
                            message = "Message. Lorem ipsum dolor sit amet",
                            timestamp = "09:45"
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}
