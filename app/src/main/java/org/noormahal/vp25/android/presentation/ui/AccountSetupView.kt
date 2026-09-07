package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.noormahal.ib.vakkic.enums.Gender
import org.noormahal.vp25.android.components.AccountSetup
import org.noormahal.vp25.android.presentation.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupView(
    viewModel: AccountSetupViewModel = viewModel(),
    onSubmit: () -> Unit,
) {
    var fullName by remember { mutableStateOf("") }
    var yearOfBirth by remember { mutableStateOf<Int?>(null) }
    var selectedGender by remember { mutableStateOf("") }

    val mobile by viewModel.mobile
    val isLoading by viewModel.isLoading
    val isSubmitting by viewModel.isSubmitting
    val error by viewModel.error

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails(onAlreadySetUp = onSubmit)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
                val gender = Gender.fromSerialized(selectedGender.lowercase())
                viewModel.submit(fullName.trim(), yearOfBirth!!, gender, onSuccess = onSubmit)
            },
        )
    }
}
