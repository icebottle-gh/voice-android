package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import network.chaintech.cmpcountrycodepicker.model.CountryDetails
import network.chaintech.cmpcountrycodepicker.ui.CountryPickerBasicTextField
import org.noormahal.vp25.android.theme.VpTheme

private val COUNTRY_CODE_SPACING = 4.dp
private const val DISABLED_ALPHA = 0.38f

@Composable
fun VpMobileNumberField(
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    onCountrySelected: (CountryDetails) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val selectedCountryState: MutableState<CountryDetails?> = remember { mutableStateOf(null) }

    CountryPickerBasicTextField(
        mobileNumber = mobileNumber,
        defaultCountryCode = "in",
        onMobileNumberChange = onMobileNumberChange,
        onCountrySelected = {
            selectedCountryState.value = it
            onCountrySelected(it)
        },
        showCountryFlag = false,
        showCountryCode = true,
        showCountryPhoneCode = true,
        spaceAfterCountryCode = COUNTRY_CODE_SPACING,
        spaceAfterCountryPhoneCode = COUNTRY_CODE_SPACING,
        verticalDividerColor = MaterialTheme.colorScheme.onSurface.copy(
            alpha = if (enabled) 1f else DISABLED_ALPHA
        ),
        enabled = enabled,
        label = { Text(text = "Mobile") },
        singleLine = true,
    )
}

@Preview(showBackground = true)
@Composable
fun VpMobileNumberFieldPreview() {
    VpTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpMobileNumberField(
                mobileNumber = "9876543210",
                onMobileNumberChange = {},
                onCountrySelected = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VpMobileNumberFieldDisabledPreview() {
    VpTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpMobileNumberField(
                mobileNumber = "9876543210",
                onMobileNumberChange = {},
                onCountrySelected = {},
                enabled = false,
            )
        }
    }
}
