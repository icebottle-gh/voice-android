package org.noormahal.vp25.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.noormahal.vp25.android.components.ConnectivityBanner
import org.noormahal.vp25.android.components.rememberIsOnline
import org.noormahal.vp25.android.presentation.navigation.AppNavGraph
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel
import org.noormahal.vp25.android.theme.VpTheme

class MainActivity : ComponentActivity() {
    val loginViewModel: LoginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            loginViewModel.isLoadingSession.value
        }
        enableEdgeToEdge()

        setContent {
            VpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isOnline by rememberIsOnline()
                    Column(modifier = Modifier.fillMaxSize()) {
                        ConnectivityBanner(isOnline = isOnline)
                        Box(
                            modifier = if (!isOnline) {
                                Modifier.weight(1f).consumeWindowInsets(WindowInsets.statusBars)
                            } else {
                                Modifier.weight(1f)
                            }
                        ) {
                            AppNavGraph(loginViewModel)
                        }
                    }
                }
            }
        }
    }
}
