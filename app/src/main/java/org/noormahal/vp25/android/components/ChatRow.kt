package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

private val CHAT_ROW_FAR_PADDING = 64.dp
private val CHAT_ROW_VERTICAL_PADDING = 2.dp

@Composable
fun ChatRow(
    bubble: ChatBubbleSummary,
    selected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isOutgoing = bubble.type == ChatBubbleType.OUTGOING

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = CHAT_ROW_VERTICAL_PADDING, bottom = CHAT_ROW_VERTICAL_PADDING)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (isOutgoing) CHAT_ROW_FAR_PADDING else VpSpacing.screenHorizontal,
                        end = if (isOutgoing) VpSpacing.screenHorizontal else CHAT_ROW_FAR_PADDING
                    )
            ) {
                ChatBubble(
                    item = bubble,
                    modifier = Modifier.align(if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart)
                )
            }
        }
        Box(modifier = Modifier.matchParentSize().selectionOverlay(selected))
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChatRowPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                ChatRow(
                    bubble = ChatBubbleSummary(
                        type = ChatBubbleType.OUTGOING,
                        order = ChatBubbleOrder.FIRST,
                        message = "Message. Lorem ipsum dolor sit amet",
                        timestamp = "09:45",
                        messageStatus = MessageStatus.SENT
                    )
                )
                ChatRow(
                    bubble = ChatBubbleSummary(
                        type = ChatBubbleType.OUTGOING,
                        order = ChatBubbleOrder.LAST,
                        message = "A much longer message that should wrap onto more than one line instead of stretching all the way to the far edge of the screen",
                        timestamp = "09:45",
                        messageStatus = MessageStatus.RECEIVED
                    )
                )
                ChatRow(
                    bubble = ChatBubbleSummary(
                        type = ChatBubbleType.INCOMING,
                        order = ChatBubbleOrder.FIRST,
                        message = "Message. Lorem ipsum dolor sit amet",
                        timestamp = "09:45",
                        showName = true,
                        name = "Name"
                    )
                )
                ChatRow(
                    bubble = ChatBubbleSummary(
                        type = ChatBubbleType.INCOMING,
                        order = ChatBubbleOrder.LAST,
                        message = "Message. Lorem ipsum dolor sit amet",
                        timestamp = "09:45"
                    ),
                    selected = true
                )
            }
        }
    }
}
