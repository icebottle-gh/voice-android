package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.OrangeA200
import org.noormahal.vp25.android.theme.OrangeBlack800
import org.noormahal.vp25.android.theme.VpTheme
import org.noormahal.vp25.android.theme.White

private val FAB_SIZE = 56.dp
private val SQUIRCLE_CORNER_RADIUS = 20.dp
private val BORDER_WIDTH = 2.dp
private val ELEVATION = 4.dp

enum class FabStyle {
    ROUND_PRIMARY,
    ROUND_SECONDARY,
    ROUND_OUTLINE,
    SQUIRCLE_PRIMARY,
    SQUIRCLE_SECONDARY,
    SQUIRCLE_OUTLINE
}

@Composable
fun VpFab(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: FabStyle = FabStyle.ROUND_PRIMARY
) {
    val shape = when (style) {
        FabStyle.ROUND_PRIMARY, FabStyle.ROUND_SECONDARY, FabStyle.ROUND_OUTLINE -> CircleShape
        FabStyle.SQUIRCLE_PRIMARY, FabStyle.SQUIRCLE_SECONDARY, FabStyle.SQUIRCLE_OUTLINE -> RoundedCornerShape(SQUIRCLE_CORNER_RADIUS)
    }

    val containerColor = when (style) {
        FabStyle.ROUND_PRIMARY, FabStyle.SQUIRCLE_PRIMARY -> OrangeA200
        FabStyle.ROUND_SECONDARY, FabStyle.SQUIRCLE_SECONDARY -> MaterialTheme.colorScheme.surfaceVariant
        FabStyle.ROUND_OUTLINE, FabStyle.SQUIRCLE_OUTLINE -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when (style) {
        FabStyle.ROUND_PRIMARY, FabStyle.SQUIRCLE_PRIMARY -> OrangeBlack800
        FabStyle.ROUND_SECONDARY, FabStyle.SQUIRCLE_SECONDARY -> MaterialTheme.colorScheme.onSurfaceVariant
        FabStyle.ROUND_OUTLINE, FabStyle.SQUIRCLE_OUTLINE -> MaterialTheme.colorScheme.onSurface
    }

    val border = when (style) {
        FabStyle.ROUND_OUTLINE, FabStyle.SQUIRCLE_OUTLINE -> BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.outline)
        else -> null
    }

    val elevation = if (border == null) ELEVATION else 0.dp

    Surface(
        onClick = onClick,
        modifier = modifier.size(FAB_SIZE),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        tonalElevation = elevation,
        shadowElevation = elevation
    ) {
        Row(
            modifier = Modifier.size(FAB_SIZE),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 500)
@Preview(name = "Dark", showBackground = true, widthDp = 500, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpFabPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VpFab(icon = Icons.Filled.Edit, contentDescription = "Edit", onClick = {}, style = FabStyle.ROUND_PRIMARY)
                VpFab(icon = Icons.Filled.Edit, contentDescription = "Edit", onClick = {}, style = FabStyle.ROUND_SECONDARY)
                VpFab(icon = Icons.Filled.Edit, contentDescription = "Edit", onClick = {}, style = FabStyle.ROUND_OUTLINE)
                VpFab(icon = Icons.Filled.Edit, contentDescription = "Edit", onClick = {}, style = FabStyle.SQUIRCLE_PRIMARY)
                VpFab(icon = Icons.Filled.Edit, contentDescription = "Edit", onClick = {}, style = FabStyle.SQUIRCLE_SECONDARY)
                VpFab(icon = Icons.Filled.Edit, contentDescription = "Edit", onClick = {}, style = FabStyle.SQUIRCLE_OUTLINE)
            }
        }
    }
}
