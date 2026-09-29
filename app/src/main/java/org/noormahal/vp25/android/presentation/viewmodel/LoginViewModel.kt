package org.noormahal.vp25.android.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.common.Client
import org.noormahal.vp25.android.data.EncryptedSecretStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.noormahal.ib.vakkic.AppImpl
import org.noormahal.ib.vakkic.User
import org.noormahal.ib.vakkic.UserImpl
import org.noormahal.ib.vakkic.enums.AccountState
import org.noormahal.vp25.android.presentation.navigation.PostAuthDestination
import org.noormahal.vp25.android.presentation.navigation.toPostAuthDestination

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val _isLoggedIn = MutableStateFlow(false) // Replace with actual login check
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn
    private val _isLoadingSession = MutableStateFlow(true)
    val isLoadingSession: StateFlow<Boolean> = _isLoadingSession
    private val _postAuthDestination = MutableStateFlow<PostAuthDestination?>(null)
    val postAuthDestination: StateFlow<PostAuthDestination?> = _postAuthDestination
    private val _setupMobile = MutableStateFlow<String?>(null)
    val setupMobile: StateFlow<String?> = _setupMobile
    private val secretStore: EncryptedSecretStore = EncryptedSecretStore(application)

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
        viewModelScope.launch {
            Client.sessionExpired.collect {
                logout()
            }
        }
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

    fun login(mobile: String, otp: String, onSuccess: (PostAuthDestination) -> Unit) {
        _loginError.value = null
        viewModelScope.launch {
            try {
                val destination = withContext(Dispatchers.IO) {
                    val user = Client.app.login(mobile, otp)
                    secretStore.setSecret(user.serialize())
                    Client.user = user
                    resolvePostAuthDestination(user, fallbackMobile = mobile)
                }
                _postAuthDestination.value = destination
                setLoggedIn(true)
                onSuccess(destination)
            } catch (e: Exception) {
                e.printStackTrace()
                _loginError.value = e.message ?: "Something went wrong. Please try again."
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                secretStore.clearSecret()
            }
            Client.user = null
            _postAuthDestination.value = null
            setLoggedIn(false)
        }
    }

    private fun wake() {
        viewModelScope.launch(Dispatchers.IO) {
            if (Client.user == null) {
                try {
                    val secret = secretStore.getSecret()
                    if (secret != null) {
                        val user = UserImpl.deserialize(secret, Client.app as AppImpl?)
                        Client.user = user
                        _postAuthDestination.value = resolvePostAuthDestination(user)
                        setLoggedIn(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    secretStore.clearSecret()
                    Client.user = null
                    setLoggedIn(false)
                }
            }
            setLoadingSession(false)
        }
    }

    private suspend fun resolvePostAuthDestination(user: User, fallbackMobile: String? = null): PostAuthDestination {
        val details = try {
            withContext(Dispatchers.IO) { user.account().getDetails() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        _setupMobile.value = details?.mobile ?: fallbackMobile
        return if (details == null) {
            PostAuthDestination.ACCOUNT_SETUP
        } else {
            AccountState.fromSerialized(details.accountState).toPostAuthDestination()
        }
    }
}