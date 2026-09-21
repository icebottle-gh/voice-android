package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun VpIconToggleButton(
    @DrawableRes icon: Int,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val contentColor = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onToggle)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpIconToggleButtonPreview() {
    VpTheme {
        Row {
            VpIconToggleButton(
                icon = R.drawable.baseline_format_bold_24,
                selected = false,
                onToggle = {}
            )
            Box(modifier = Modifier.width(8.dp))
            VpIconToggleButton(
                icon = R.drawable.baseline_format_bold_24,
                selected = true,
                onToggle = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpIconToggleButtonGroupPreview() {
    VpTheme {
        Row {
            VpIconToggleButton(
                icon = R.drawable.baseline_format_bold_24,
                selected = true,
                onToggle = {}
            )
            Box(modifier = Modifier.width(8.dp))
            VpIconToggleButton(
                icon = R.drawable.baseline_format_italic_24,
                selected = false,
                onToggle = {}
            )
            Box(modifier = Modifier.width(8.dp))
            VpIconToggleButton(
                icon = R.drawable.baseline_format_strikethrough_24,
                selected = false,
                onToggle = {}
            )
        }
    }
}
