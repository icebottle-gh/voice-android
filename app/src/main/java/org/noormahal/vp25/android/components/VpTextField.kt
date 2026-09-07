package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

private val TEXT_FIELD_CORNER_RADIUS = 10.dp
private val NAME_ALLOWED_SYMBOLS = charArrayOf('\'', '-', '.')

@Composable
fun VpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLength: Int? = null,
    digitOnly: Boolean = false,
    lettersOnly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val filtered = when {
                digitOnly -> input.filter { it.isDigit() }
                lettersOnly -> input.filter { it.isLetter() || it.isWhitespace() || it in NAME_ALLOWED_SYMBOLS }
                else -> input
            }
            if (maxLength == null || filtered.length <= maxLength) onValueChange(filtered)
        },
        label = { Text(text = label) },
        placeholder = placeholder?.let { { Text(text = it) } },
        modifier = modifier,
        singleLine = minLines <= 1,
        minLines = minLines,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        supportingText = supportingText?.let { { Text(text = it) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(TEXT_FIELD_CORNER_RADIUS),
    )
}


@Preview(name = "Light", showBackground = true, widthDp = 360)
@Preview(name = "Dark", showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpTextFieldVariantsPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Label only, empty
                VpTextField(
                    value = "",
                    onValueChange = {},
                    label = "Full Name",
                )

                // Label + placeholder
                VpTextField(
                    value = "",
                    onValueChange = {},
                    label = "Full Name",
                    placeholder = "e.g. Jane Doe",
                )

                // Leading icon (e.g. Year of Birth)
                VpTextField(
                    value = "2001",
                    onValueChange = {},
                    label = "Year Of Birth",
                    leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                )

                // Trailing icon, read-only (e.g. Gender dropdown anchor)
                VpTextField(
                    value = "",
                    onValueChange = {},
                    label = "Gender",
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                )

                // Disabled with a pre-filled value (e.g. Mobile)
                VpTextField(
                    value = "+91 9876543210",
                    onValueChange = {},
                    label = "Mobile",
                    enabled = false,
                )

                // Error + supporting text
                VpTextField(
                    value = "",
                    onValueChange = {},
                    label = "Full Name",
                    isError = true,
                    supportingText = "This field is required",
                )

                // Multiline
                VpTextField(
                    value = "",
                    onValueChange = {},
                    label = "Bio",
                    minLines = 3,
                )
            }
        }
    }
}
