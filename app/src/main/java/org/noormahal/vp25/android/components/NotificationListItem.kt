package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

data class NotificationSummary(
    @DrawableRes val icon: Int,
    val highlightedText: String,
    val regularText: String,
    val time: String
)

@Composable
fun NotificationListItem(
    notification: NotificationSummary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = notification.icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(notification.highlightedText)
                }
                append(" ")
                append(notification.regularText)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = notification.time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationListItemPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                NotificationListItem(
                    notification = NotificationSummary(
                        icon = R.drawable.baseline_dynamic_feed_24,
                        highlightedText = "Lorem ipsum",
                        regularText = "dolor sit amet, sed do eius mod tempor incididunt ut labore",
                        time = "Time"
                    )
                )
                NotificationListItem(
                    notification = NotificationSummary(
                        icon = R.drawable.baseline_chat_bubble_24,
                        highlightedText = "Lorem ipsum",
                        regularText = "dolor sit amet, sed do eius mod tempor incididunt ut labore",
                        time = "Time"
                    )
                )
                NotificationListItem(
                    notification = NotificationSummary(
                        icon = R.drawable.baseline_person_24,
                        highlightedText = "Lorem ipsum",
                        regularText = "dolor sit amet, sed do eius mod tempor incididunt ut labore",
                        time = "Time"
                    )
                )
            }
        }
    }
}
