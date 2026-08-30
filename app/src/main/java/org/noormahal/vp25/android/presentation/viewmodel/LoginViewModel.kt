package org.noormahal.vp25.android.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.common.Client
import org.noormahal.vp25.android.data.AppSecretDao
import org.noormahal.vp25.android.data.VakkiDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.noormahal.ib.vakkic.AppImpl
import org.noormahal.ib.vakkic.UserImpl

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val _isLoggedIn = MutableStateFlow(false) // Replace with actual login check
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn
    private val _isLoadingSession = MutableStateFlow(true)
    val isLoadingSession: StateFlow<Boolean> = _isLoadingSession
    private val appSecretDao: AppSecretDao = VakkiDatabase.getDatabase(application).appSecretDao()

    private val _otp = mutableStateOf("")
    val otp: State<String> = _otp
    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError
    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }
    fun setLoadingSession(loading: Boolean) {
        _isLoadingSession.value = loading
    }

    init {
        wake()
    }

    fun requestOtp(mobile: String) {
        viewModelScope.launch {
            try {
                val otp = withContext(Dispatchers.IO) {
                    Client.app.requestLoginOtp(mobile)
                    Client.app.peekOtp(mobile)
                }
                _otp.value = otp
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(mobile: String, otp: String, onSuccess: () -> Unit) {
        _loginError.value = null
        viewModelScope.launch {
            try {
                val loggedInUser = withContext(Dispatchers.IO) {
                    val user = Client.app.login(mobile, otp)
                    appSecretDao.setSecret(user.serialize())
                    user
                }
                Client.user = loggedInUser
                setLoggedIn(true)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _loginError.value = e.message ?: "Something went wrong. Please try again."
            }
        }
    }

    private fun wake() {
        viewModelScope.launch(Dispatchers.IO) {
            if (Client.user == null) {
                val secret = appSecretDao.getSecret()?.secretValue
                if (secret != null) {
                    Client.user = UserImpl.deserialize(secret, Client.app as AppImpl?)
                    setLoggedIn(true)
                }
            }
            setLoadingSession(false)
        }
    }
}