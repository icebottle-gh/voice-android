package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpTheme

enum class VpBadgeType { Number, Icon, Empty }

private val BADGE_SIZE = 20.dp
private val EMPTY_BADGE_SIZE = 8.dp
private val BADGE_ICON_SIZE = 12.dp
private val BADGE_NUMBER_HORIZONTAL_PADDING = 5.dp
private const val MAX_BADGE_NUMBER = 99

@Composable
fun VpBadge(
    type: VpBadgeType,
    modifier: Modifier = Modifier,
    number: Int? = null,
    icon: Painter? = null,
) {
    when (type) {
        VpBadgeType.Number -> {
            Box(
                modifier = modifier
                    .defaultMinSize(minWidth = BADGE_SIZE, minHeight = BADGE_SIZE)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                    .padding(horizontal = BADGE_NUMBER_HORIZONTAL_PADDING),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number?.coerceAtMost(MAX_BADGE_NUMBER)?.toString().orEmpty(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        VpBadgeType.Icon -> {
            Box(
                modifier = modifier
                    .size(BADGE_SIZE)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(BADGE_ICON_SIZE)
                    )
                }
            }
        }

        VpBadgeType.Empty -> {
            Box(
                modifier = modifier
                    .size(EMPTY_BADGE_SIZE)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpBadgePreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VpBadge(type = VpBadgeType.Number, number = 9)
                VpBadge(type = VpBadgeType.Number, number = 36)
                VpBadge(type = VpBadgeType.Number, number = 150)
                VpBadge(
                    type = VpBadgeType.Icon,
                    icon = painterResource(id = R.drawable.outline_check_24)
                )
                VpBadge(type = VpBadgeType.Empty)
            }
        }
    }
}
