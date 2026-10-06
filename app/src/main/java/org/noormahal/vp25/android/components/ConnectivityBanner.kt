package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.data.isOnlineFlow
import org.noormahal.vp25.android.theme.VpTheme

/**
 * Hoisted up to MainActivity (not just read inside ConnectivityBanner) because the caller also
 * needs to know whether to consume the status-bar inset for the content below the banner - see
 * ConnectivityBanner's doc.
 */
@Composable
fun rememberIsOnline(): State<Boolean> {
    val context = LocalContext.current
    return remember(context) { isOnlineFlow(context) }.collectAsState(initial = true)
}

/**
 * Persistent (not auto-dismissing) banner for "device has no internet at all" - stays up for as
 * long as the condition holds, unlike a Snackbar/Toast. Lives above AppNavGraph in MainActivity
 * rather than inside any one screen's chrome, since plenty of screens (StoriesDetail, Login,
 * the session-retry screen) don't have a top bar to anchor to.
 */
@Composable
fun ConnectivityBanner(isOnline: Boolean) {
    AnimatedVisibility(
        visible = !isOnline,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityBannerContent()
    }
}

@Composable
private fun ConnectivityBannerContent() {
    Surface(color = MaterialTheme.colorScheme.errorContainer) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "No internet connection",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConnectivityBannerPreview() {
    VpTheme {
        ConnectivityBannerContent()
    }
}

