package org.noormahal.vp25.android.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

private const val DEFAULT_LOADING_INDICATOR_DELAY_MS = 300L

/**
 * Avoids flashing a loading indicator for fast loads - only turns true once [isLoading] has
 * been true continuously for [delayMillis], so a quick response never shows a spinner at all.
 */
@Composable
fun rememberDelayedLoading(isLoading: Boolean, delayMillis: Long = DEFAULT_LOADING_INDICATOR_DELAY_MS): Boolean {
    var showLoading by remember { mutableStateOf(false) }
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(delayMillis)
            showLoading = true
        } else {
            showLoading = false
        }
    }
    return showLoading
}
