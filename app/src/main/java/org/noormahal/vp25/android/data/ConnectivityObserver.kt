package org.noormahal.vp25.android.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * True only when the system's current default network has verified internet access (a
 * successful connectivity check, not just "a wifi radio is on"). Emits false immediately, then
 * updates live as connectivity changes.
 *
 * Each callback is trusted as the signal on its own - never re-queries activeNetwork/
 * getNetworkCapabilities from inside a callback. That ambient state isn't guaranteed to already
 * reflect what the callback is reporting, which is exactly the kind of race that works by luck
 * on the first transition and goes stale on a later one.
 */
fun isOnlineFlow(context: Context): Flow<Boolean> = callbackFlow {
    val connectivityManager = context.applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            trySend(false)
        }

        override fun onUnavailable() {
            trySend(false)
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            trySend(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        }
    }

    connectivityManager.registerDefaultNetworkCallback(callback)
    trySend(false)

    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
}.distinctUntilChanged()
