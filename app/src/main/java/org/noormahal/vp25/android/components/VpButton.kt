package org.noormahal.vp25.android.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.Grey300
import org.noormahal.vp25.android.theme.GreyA700
import org.noormahal.vp25.android.theme.OrangeA200
import org.noormahal.vp25.android.theme.White

// Constants
private val ROUND_CORNER_RADIUS = 24.dp
private val SQUARE_CORNER_RADIUS = 12.dp
private val FRAMELESS_CORNER_RADIUS = 0.dp

private val BUTTON_HORIZONTAL_PADDING = 24.dp
private val BUTTON_VERTICAL_PADDING = 12.dp
private val FRAMELESS_HORIZONTAL_PADDING = 16.dp
private val FRAMELESS_VERTICAL_PADDING = 8.dp

private val BORDER_WIDTH_ENABLED = 1.dp
private val BORDER_WIDTH_DISABLED = 0.5.dp

private const val DISABLED_ALPHA = 0.50f

enum class ButtonStyle {
    ROUND_PRIMARY,
    SQUARE_PRIMARY,
    ROUND_SECONDARY,
    SQUARE_SECONDARY,
    ROUND_PRIMARY_OUTLINE,
    SQUARE_PRIMARY_OUTLINE,
    PRIMARY_FRAMELESS
}

@Composable
fun VpButton(
    label: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.ROUND_PRIMARY,
    enabled: Boolean = true,
    fullWidth: Boolean = false
) {
    val finalModifier = if (fullWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }
    
    val cornerRadius = when (style) {
        ButtonStyle.ROUND_PRIMARY, ButtonStyle.ROUND_SECONDARY,
        ButtonStyle.ROUND_PRIMARY_OUTLINE -> ROUND_CORNER_RADIUS
        ButtonStyle.SQUARE_PRIMARY, ButtonStyle.SQUARE_SECONDARY,
        ButtonStyle.SQUARE_PRIMARY_OUTLINE -> SQUARE_CORNER_RADIUS
        ButtonStyle.PRIMARY_FRAMELESS -> FRAMELESS_CORNER_RADIUS
    }

    val contentColor = when (style) {
        ButtonStyle.ROUND_PRIMARY, ButtonStyle.SQUARE_PRIMARY -> White
        ButtonStyle.ROUND_SECONDARY, ButtonStyle.SQUARE_SECONDARY -> GreyA700
        ButtonStyle.ROUND_PRIMARY_OUTLINE, ButtonStyle.SQUARE_PRIMARY_OUTLINE, ButtonStyle.PRIMARY_FRAMELESS -> OrangeA200
    }

    when (style) {
        ButtonStyle.ROUND_PRIMARY, ButtonStyle.SQUARE_PRIMARY,
        ButtonStyle.ROUND_SECONDARY, ButtonStyle.SQUARE_SECONDARY -> {
            val backgroundColor = when (style) {
                ButtonStyle.ROUND_PRIMARY, ButtonStyle.SQUARE_PRIMARY -> OrangeA200
                else -> Grey300
            }
            
            Button(
                onClick = onClick,
                modifier = finalModifier,
                enabled = enabled,
                shape = RoundedCornerShape(cornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor,
                    disabledContainerColor = Grey300.copy(alpha = DISABLED_ALPHA),
                    disabledContentColor = GreyA700.copy(alpha = DISABLED_ALPHA)
                ),
                contentPadding = PaddingValues(
                    horizontal = BUTTON_HORIZONTAL_PADDING, 
                    vertical = BUTTON_VERTICAL_PADDING
                )
            ) {
                label()
            }
        }

        ButtonStyle.ROUND_PRIMARY_OUTLINE, ButtonStyle.SQUARE_PRIMARY_OUTLINE -> {
            OutlinedButton(
                onClick = onClick,
                modifier = finalModifier,
                enabled = enabled,
                shape = RoundedCornerShape(cornerRadius),
                border = BorderStroke(
                    width = if (enabled) BORDER_WIDTH_ENABLED else BORDER_WIDTH_DISABLED,
                    color = if (enabled) contentColor else GreyA700.copy(alpha = DISABLED_ALPHA)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = contentColor,
                    disabledContentColor = GreyA700.copy(alpha = DISABLED_ALPHA)
                ),
                contentPadding = PaddingValues(
                    horizontal = BUTTON_HORIZONTAL_PADDING, 
                    vertical = BUTTON_VERTICAL_PADDING
                )
            ) {
                label()
            }
        }

        ButtonStyle.PRIMARY_FRAMELESS -> {
            TextButton(
                onClick = onClick,
                modifier = finalModifier,
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = contentColor,
                    disabledContentColor = contentColor.copy(alpha = DISABLED_ALPHA)
                ),
                contentPadding = PaddingValues(
                    horizontal = FRAMELESS_HORIZONTAL_PADDING,
                    vertical = FRAMELESS_VERTICAL_PADDING
                )
            ) {
                label()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800)
@Composable
fun VpButtonPreview() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Enabled Column
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.ROUND_PRIMARY)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.SQUARE_PRIMARY)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.ROUND_SECONDARY)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.SQUARE_SECONDARY)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.SQUARE_PRIMARY_OUTLINE)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.PRIMARY_FRAMELESS)
        }
        
        // Disabled Column
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.ROUND_PRIMARY, enabled = false)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.SQUARE_PRIMARY, enabled = false)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.ROUND_SECONDARY, enabled = false)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.SQUARE_SECONDARY, enabled = false)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE, enabled = false)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.SQUARE_PRIMARY_OUTLINE, enabled = false)
            VpButton(label = { Text("Button") }, onClick = {}, style = ButtonStyle.PRIMARY_FRAMELESS, enabled = false)
        }
    }
}
