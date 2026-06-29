package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.TempTheme

private val TEXT_FIELD_CORNER_RADIUS = 10.dp

@Composable
fun VpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    maxLength: Int? = null,
    digitOnly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val filtered = if (digitOnly) input.filter { it.isDigit() } else input
            if (maxLength == null || filtered.length <= maxLength) onValueChange(filtered)
        },
        label = { Text(text = label) },
        modifier = modifier,
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(TEXT_FIELD_CORNER_RADIUS),
    )
}

@Preview(showBackground = true)
@Composable
fun VpTextFieldEmailPreview() {
    TempTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpTextField(
                value = "user@example.com",
                onValueChange = {},
                label = "Email",
                keyboardType = KeyboardType.Email,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VpTextFieldEmailDisabledPreview() {
    TempTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpTextField(
                value = "user@example.com",
                onValueChange = {},
                label = "Email",
                keyboardType = KeyboardType.Email,
                enabled = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VpTextFieldOtpPreview() {
    TempTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpTextField(
                value = "123456",
                onValueChange = {},
                label = "OTP",
                keyboardType = KeyboardType.Number,
                digitOnly = true,
                maxLength = 6,
            )
        }
    }
}
