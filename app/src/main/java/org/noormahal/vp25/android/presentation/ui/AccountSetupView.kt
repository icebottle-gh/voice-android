package org.noormahal.vp25.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.noormahal.ib.vakkic.enums.Gender
import org.noormahal.vp25.android.components.AccountSetup
import org.noormahal.vp25.android.presentation.viewmodel.AccountSetupViewModel

private val genderOptionsToEnum = linkedMapOf(
    "Male" to Gender.MALE,
    "Female" to Gender.FEMALE,
    "Other" to Gender.OTHER,
)

@Composable
fun AccountSetupView(
    viewModel: AccountSetupViewModel = viewModel(),
    onSubmit: () -> Unit,
) {
    var fullName by remember { mutableStateOf("") }
    var yearOfBirth by remember { mutableStateOf<Int?>(null) }
    var selectedGender by remember { mutableStateOf("") }

    val mobile by viewModel.mobile
    val isSubmitting by viewModel.isSubmitting
    val error by viewModel.error

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails(onAlreadySetUp = onSubmit)
    }

    AccountSetup(
        mobile = mobile,
        fullName = fullName,
        onFullNameChange = { fullName = it },
        yearOfBirth = yearOfBirth,
        onYearOfBirthChange = { yearOfBirth = it },
        selectedGender = selectedGender,
        onGenderChange = { selectedGender = it },
        isSubmitting = isSubmitting,
        errorMessage = error,
        onSubmit = {
            val gender = genderOptionsToEnum.getValue(selectedGender)
            viewModel.submit(fullName.trim(), yearOfBirth!!, gender, onSuccess = onSubmit)
        },
    )
}
