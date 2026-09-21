package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    if (status == MessageStatus.RECEIVED) {
        DoubleCheckCircleIcon(
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
        return
    }

    val (icon, contentDescription) = when (status) {
        MessageStatus.SENDING -> R.drawable.baseline_schedule_24 to "Sending"
        MessageStatus.SENT -> R.drawable.outline_check_circle_24 to "Sent"
        MessageStatus.ERROR -> R.drawable.outline_error_24 to "Failed to send"
        MessageStatus.RECEIVED -> error("handled above")
    }

    val tint = if (status == MessageStatus.ERROR) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Icon(
        painter = painterResource(id = icon),
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(16.dp)
    )
}

@Composable
private fun DoubleCheckCircleIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 22.dp, height = 16.dp)
            .semantics { contentDescription = "Received" }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_check_circle_24),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterStart)
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    // Punch a transparent hole where the front icon will sit, so the
                    // two rings look stacked instead of their outlines intersecting.
                    drawCircle(
                        color = Color.Black,
                        radius = size.minDimension * 0.5f,
                        center = Offset(size.width, size.height / 2f),
                        blendMode = BlendMode.Clear
                    )
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.outline_check_circle_24),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterEnd)
        )
    }
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
