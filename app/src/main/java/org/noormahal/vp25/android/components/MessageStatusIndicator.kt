package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpTheme

enum class MessageStatus { SENDING, SENT, RECEIVED, ERROR }

@Composable
fun MessageStatusIndicator(
    status: MessageStatus,
    modifier: Modifier = Modifier
) {
    val icon = when (status) {
        MessageStatus.SENDING -> R.drawable.baseline_schedule_24
        MessageStatus.SENT -> R.drawable.outline_check_circle_24
        MessageStatus.RECEIVED -> R.drawable.baseline_check_circle_24
        MessageStatus.ERROR -> R.drawable.outline_error_24
    }

    val tint = when (status) {
        MessageStatus.SENDING, MessageStatus.SENT -> MaterialTheme.colorScheme.onSurfaceVariant
        MessageStatus.RECEIVED -> MaterialTheme.colorScheme.onSurfaceVariant
        MessageStatus.ERROR -> MaterialTheme.colorScheme.error
    }

    val contentDescription = when (status) {
        MessageStatus.SENDING -> "Sending"
        MessageStatus.SENT -> "Sent"
        MessageStatus.RECEIVED -> "Received"
        MessageStatus.ERROR -> "Failed to send"
    }

    Icon(
        painter = painterResource(id = icon),
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(16.dp)
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MessageStatusIndicatorPreview() {
    VpTheme {
        Row {
            MessageStatusIndicator(status = MessageStatus.SENDING)
            Spacer(modifier = Modifier.width(8.dp))
            MessageStatusIndicator(status = MessageStatus.SENT)
            Spacer(modifier = Modifier.width(8.dp))
            MessageStatusIndicator(status = MessageStatus.RECEIVED)
            Spacer(modifier = Modifier.width(8.dp))
            MessageStatusIndicator(status = MessageStatus.ERROR)
        }
    }
}
