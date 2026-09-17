package org.noormahal.vp25.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

enum class VpTagType { Default, Focused }

@Composable
fun VpTag(
    text: String,
    modifier: Modifier = Modifier,
    type: VpTagType = VpTagType.Default,
    showLeftIcon: Boolean = false,
    leftIcon: ImageVector? = null,
    showRightIcon: Boolean = false,
    rightIcon: ImageVector? = null
) {
    val containerColor = when (type) {
        VpTagType.Default -> MaterialTheme.colorScheme.surfaceVariant
        VpTagType.Focused -> MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = when (type) {
        VpTagType.Default -> MaterialTheme.colorScheme.onSurfaceVariant
        VpTagType.Focused -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Row(
        modifier = modifier
            .background(color = containerColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLeftIcon && leftIcon != null) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
        if (showRightIcon && rightIcon != null) {
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                imageVector = rightIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview
@Composable
fun VpTagDefaultPreview() {
    VpTheme{
        VpTag(text = "Private")
    }
}

@Preview
@Composable
fun VpTagFocusedPreview() {
    VpTheme{
        VpTag(text = "Private", type = VpTagType.Focused)
    }
}

@Preview
@Composable
fun VpTagWithIconsPreview() {
    VpTheme{
        VpTag(
            text = "Tag",
            showLeftIcon = true,
            leftIcon = Icons.Filled.Edit,
            showRightIcon = true,
            rightIcon = Icons.Filled.Edit
        )
    }
}
