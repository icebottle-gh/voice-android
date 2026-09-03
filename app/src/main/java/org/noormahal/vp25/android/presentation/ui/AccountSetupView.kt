package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.components.ButtonStyle
import org.noormahal.vp25.android.components.VpButton
import org.noormahal.vp25.android.components.VpTextField
import org.noormahal.vp25.android.components.VpYearPicker
import org.noormahal.vp25.android.theme.VpTheme

private val FIELD_SPACING = 12.dp
private const val MIN_FULL_NAME_LENGTH = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSetupView(onSubmit: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var fullNameTouched by remember { mutableStateOf(false) }
    var yearOfBirth by remember { mutableStateOf<Int?>(null) }

    val genderOptions = listOf("Male", "Female", "Other")
    var genderExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }

    // Mobile is already verified at login; this screen only displays it.
    // TODO: populate from the logged-in user once that's wired up.
    val mobile by remember { mutableStateOf("") }

    val isFullNameValid = fullName.trim().length >= MIN_FULL_NAME_LENGTH
    val fullNameError = fullNameTouched && !isFullNameValid

    val isFormValid = isFullNameValid && yearOfBirth != null && selectedGender.isNotBlank()

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .systemBarsPadding()
            .padding(horizontal = 64.dp, vertical = 24.dp)
            .imePadding()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Let's get you set up",
            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Thin,
        )

        Spacer(modifier = Modifier.height(32.dp))

        VpTextField(
            value = mobile,
            onValueChange = {},
            label = "Mobile",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Phone,
            enabled = false,
        )
        Spacer(modifier = Modifier.height(FIELD_SPACING))

        VpTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                fullNameTouched = true
            },
            label = "Full Name",
            modifier = Modifier.fillMaxWidth(),
            lettersOnly = true,
            isError = fullNameError,
            supportingText = if (fullNameError) {
                "Enter at least $MIN_FULL_NAME_LENGTH characters"
            } else {
                null
            },
        )
        Spacer(modifier = Modifier.height(FIELD_SPACING))

        VpYearPicker(
            value = yearOfBirth,
            onValueChange = { yearOfBirth = it },
            label = "Year Of Birth",
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(FIELD_SPACING))

        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            VpTextField(
                value = selectedGender,
                onValueChange = {},
                label = "Gender",
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
            )

            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            selectedGender = selectionOption
                            genderExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        VpButton(
            label = { Text(text = "Submit") },
            onClick = onSubmit,
            style = ButtonStyle.SQUARE_PRIMARY,
            enabled = isFormValid,
            fullWidth = true,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AccountSetupPreview() {
    VpTheme {
        AccountSetupView {}
    }
}
