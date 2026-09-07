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

class AccountSetupViewModel : ViewModel() {
    private val _mobile = mutableStateOf("")
    val mobile: State<String> = _mobile
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading
    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadAccountDetails(onAlreadySetUp: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val details = withContext(Dispatchers.IO) {
                    Client.user!!.account().getDetails()
                }
                if (AccountState.fromSerialized(details.accountState) != AccountState.SETUP) {
                    onAlreadySetUp()
                } else {
                    _mobile.value = details.mobile.orEmpty()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "Something went wrong. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submit(fullName: String, yearOfBirth: Int, gender: Gender, onSuccess: () -> Unit) {
        _error.value = null
        _isSubmitting.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    Client.user!!.account().setDetails(fullName, yearOfBirth.toString(), gender)
                }
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "Something went wrong. Please try again."
            } finally {
                _isSubmitting.value = false
            }
        }
    }
}
