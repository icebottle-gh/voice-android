package org.noormahal.vp25.android.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.noormahal.ib.vakkic.ApiException
import org.noormahal.ib.vakkic.App
import org.noormahal.ib.vakkic.AppImpl
import org.noormahal.ib.vakkic.ErrorCode
import org.noormahal.ib.vakkic.User

object Client {
    var app: App = AppImpl("https://ib-service.noormahal.org/ib-api/vakki", "9ciBrYwZePyjgDnutVoaDci9LGiHy6uJKV")
    var user: User? = null

    private val _sessionExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpired: SharedFlow<Unit> = _sessionExpired.asSharedFlow()

    // Call from a catch block around any authenticated API call. If the failure means the
    // server no longer honors our token, this signals LoginViewModel to log out.
    fun reportIfUnauthorized(error: Throwable) {
        if (error is ApiException && (error.httpStatusCode == 401 || error.errorCode == ErrorCode.EXC_TOK)) {
            _sessionExpired.tryEmit(Unit)
        }
    }
}
