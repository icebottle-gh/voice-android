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
import org.noormahal.vp25.android.presentation.navigation.PostAuthDestination
import org.noormahal.vp25.android.presentation.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupView(
    initialMobile: String? = null,
    viewModel: AccountSetupViewModel = viewModel(),
    onSubmit: (PostAuthDestination) -> Unit,
) {
    var fullName by remember { mutableStateOf("") }
    var yearOfBirth by remember { mutableStateOf<Int?>(null) }
    var selectedGender by remember { mutableStateOf("") }

    val fetchedMobile by viewModel.mobile
    val isSubmitting by viewModel.isSubmitting
    val error by viewModel.error

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails()
    }

    // Already known from login (or a discarded fetch during session restore) - shown right away
    // instead of blocking the form on a redundant re-fetch. Once loadAccountDetails() resolves,
    // its result naturally takes over since it's no longer blank.
    val displayMobile = fetchedMobile.ifBlank { initialMobile.orEmpty() }

    AccountSetup(
        mobile = displayMobile,
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
