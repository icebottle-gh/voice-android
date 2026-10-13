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
    private val _sessionError = MutableStateFlow<String?>(null)
    val sessionError: StateFlow<String?> = _sessionError
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
                if (destination == null) {
                    // Login itself succeeded - we just couldn't confirm account state (e.g. a
                    // slow/unreachable backend). Let them retry rather than guessing a
                    // destination, since a wrong guess here would misroute a brand-new vs.
                    // established account.
                    _loginError.value = "Couldn't confirm your account status. Please try again."
                    return@launch
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
                        resolveSession(user)
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

    // Re-resolves account state for the already-deserialized session, without repeating the
    // secret lookup - lets the user retry after a failed resolveSession() instead of the app
    // guessing a destination (or the app forcing them back through login) on a network hiccup.
    fun retryResolveSession() {
        val user = Client.user ?: return
        _sessionError.value = null
        viewModelScope.launch(Dispatchers.IO) {
            resolveSession(user)
        }
    }

    private suspend fun resolveSession(user: User) {
        val destination = resolvePostAuthDestination(user)
        if (destination == null) {
            // Couldn't confirm account state (e.g. a slow/unreachable backend) - neither HOME
            // nor ACCOUNT_SETUP is a safe guess here, since either could be wrong depending on
            // whether this account actually finished setup. Leave isLoggedIn false so the UI
            // stays on a retry state instead of misrouting into the app.
            _sessionError.value = "Couldn't verify your account. Check your connection and try again."
            return
        }
        _postAuthDestination.value = destination
        _sessionError.value = null
        setLoggedIn(true)
    }

    // Returns null only when the getDetails() call itself fails (network/backend issue) - a
    // never-set-up account still fetches successfully, with accountState reflecting that (it
    // serializes as "incomplete" and maps to ACCOUNT_SETUP below), so null is never a valid
    // "this account needs setup" signal and callers must not treat it as one.
    private suspend fun resolvePostAuthDestination(user: User, fallbackMobile: String? = null): PostAuthDestination? {
        val details = try {
            withContext(Dispatchers.IO) { user.account().getDetails() }
        } catch (e: Exception) {
            e.printStackTrace()
            Client.reportIfUnauthorized(e)
            return null
        }
        _setupMobile.value = details.mobile ?: fallbackMobile
        return AccountState.fromSerialized(details.accountState).toPostAuthDestination()
    }
}