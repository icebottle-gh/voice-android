package com.example.temp.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.temp.common.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.ApiException

class LoginViewModel: ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false) // Replace with actual login check
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _otp = mutableStateOf("")
    val otp: State<String> = _otp
    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    fun requestOtp(mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("calling api")
                Client.app.requestLoginOtp(mobile)
                _otp.value = Client.app.peekOtp(mobile)
                println(_otp.value)
            } catch (e: ApiException) {
                // handle error
                println(e)
            }

        }
    }

    fun login(mobile: String, otp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Client.user = Client.app.login(mobile, otp)
            } catch (e: ApiException) {
                println(e)
            }
        }
    }
}