package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpTheme

enum class VpSearchFieldStyle { Outlined, Filled }

private val SEARCH_FIELD_SHAPE = RoundedCornerShape(percent = 50)

@Composable
fun VpSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    style: VpSearchFieldStyle = VpSearchFieldStyle.Outlined,
    enabled: Boolean = true,
    showLeadingIcon: Boolean = true,
    showClearButton: Boolean = true,
) {
    val outlined = style == VpSearchFieldStyle.Outlined
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        !enabled -> if (outlined) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f) else Color.Transparent
        isFocused -> MaterialTheme.colorScheme.primary
        outlined -> MaterialTheme.colorScheme.outline
        else -> Color.Transparent
    }
    val borderWidth = if (isFocused) 2.dp else 1.dp
    val containerColor = when {
        outlined -> Color.Transparent
        !enabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 1f else 0.38f)
    val placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.38f)
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.38f)

    Row(
        modifier = modifier
            .background(containerColor, SEARCH_FIELD_SHAPE)
            .border(borderWidth, borderColor, SEARCH_FIELD_SHAPE)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLeadingIcon) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = placeholderColor
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (showClearButton && value.isNotEmpty() && enabled) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.outline_cancel_24),
                contentDescription = "Clear text",
                tint = iconTint,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onValueChange("") }
            )
        }
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 360)
@Preview(name = "Dark", showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpSearchFieldVariantsPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VpSearchField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Label",
                    modifier = Modifier.fillMaxWidth()
                )
                VpSearchField(
                    value = "Searching",
                    onValueChange = {},
                    placeholder = "Label",
                    modifier = Modifier.fillMaxWidth()
                )
                VpSearchField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Label",
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                VpSearchField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Label",
                    style = VpSearchFieldStyle.Filled,
                    modifier = Modifier.fillMaxWidth()
                )
                VpSearchField(
                    value = "Searching",
                    onValueChange = {},
                    placeholder = "Label",
                    style = VpSearchFieldStyle.Filled,
                    modifier = Modifier.fillMaxWidth()
                )
                VpSearchField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Label",
                    style = VpSearchFieldStyle.Filled,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
