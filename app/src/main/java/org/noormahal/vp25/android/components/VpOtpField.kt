package org.noormahal.vp25.android.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

private val OTP_BOX_SIZE = 48.dp
private val OTP_BOX_SPACING = 8.dp
private val OTP_BOX_CORNER_RADIUS = 8.dp
private val OTP_BORDER_WIDTH = 1.dp
private val OTP_FOCUSED_BORDER_WIDTH = 2.dp

@Composable
fun VpOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() }
            if (filtered.length <= length) onValueChange(filtered)
        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = modifier.onFocusChanged { isFocused = it.isFocused },
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(OTP_BOX_SPACING)) {
                repeat(length) { index ->
                    OtpBox(
                        char = value.getOrNull(index),
                        isActive = isFocused && index == value.length,
                        enabled = enabled,
                        isError = isError,
                    )
                }
            }
        }
    )
}

@Composable
private fun OtpBox(
    char: Char?,
    isActive: Boolean,
    enabled: Boolean,
    isError: Boolean,
) {
    val borderColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
        isError  -> MaterialTheme.colorScheme.error
        isActive -> MaterialTheme.colorScheme.primary
        else     -> MaterialTheme.colorScheme.outlineVariant
    }
    val borderWidth = if (isActive) OTP_FOCUSED_BORDER_WIDTH else OTP_BORDER_WIDTH

    val textColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        isError  -> MaterialTheme.colorScheme.error
        else     -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(OTP_BOX_SIZE)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(OTP_BOX_CORNER_RADIUS),
            )
    ) {
        if (char != null) {
            Text(
                text = char.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun VpOtpFieldEmptyPreview() {
    VpTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            VpOtpField(value = "", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VpOtpFieldPartialPreview() {
    VpTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            VpOtpField(value = "123", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VpOtpFieldFullPreview() {
    VpTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            VpOtpField(value = "123456", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VpOtpFieldDisabledPreview() {
    VpTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            VpOtpField(value = "123456", onValueChange = {}, enabled = false)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VpOtpFieldErrorPreview() {
    VpTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            VpOtpField(value = "123456", onValueChange = {}, isError = true)
        }
    }
}
