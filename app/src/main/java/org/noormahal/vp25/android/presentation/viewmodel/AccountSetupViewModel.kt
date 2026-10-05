package org.noormahal.vp25.android.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.noormahal.ib.vakkic.enums.AccountState
import org.noormahal.ib.vakkic.enums.Gender
import org.noormahal.vp25.android.common.Client
import org.noormahal.vp25.android.presentation.navigation.PostAuthDestination
import org.noormahal.vp25.android.presentation.navigation.toPostAuthDestination

class AccountSetupViewModel : ViewModel() {
    private val _mobile = mutableStateOf("")
    val mobile: State<String> = _mobile
    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Runs as a background refresh, not a blocking load - the screen already has a mobile
    // number to show from login (see AccountSetupView's initialMobile). A failure here shouldn't
    // surface as a form error since nothing the user did caused it and nothing is actually broken.
    fun loadAccountDetails() {
        viewModelScope.launch {
            try {
                val details = withContext(Dispatchers.IO) {
                    Client.user!!.account().getDetails()
                }
                _mobile.value = details.mobile.orEmpty()
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
            }
        }
    }

    fun submit(fullName: String, yearOfBirth: Int, gender: Gender, onSuccess: (PostAuthDestination) -> Unit) {
        _error.value = null
        _isSubmitting.value = true
        viewModelScope.launch {
            try {
                val updated = withContext(Dispatchers.IO) {
                    Client.user!!.account().setDetails(fullName, yearOfBirth.toString(), gender)
                }
                onSuccess(AccountState.fromSerialized(updated.accountState).toPostAuthDestination())
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
                _error.value = e.message ?: "Something went wrong. Please try again."
            } finally {
                _isSubmitting.value = false
            }
        }
    }
}
