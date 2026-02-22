package org.noormahal.vp25.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.noormahal.vp25.android.presentation.navigation.AppNavGraph
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel
import org.noormahal.vp25.android.theme.TempTheme

class MainActivity : ComponentActivity() {
    val loginViewModel: LoginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            loginViewModel.isLoadingSession.value
        }
        enableEdgeToEdge()
        //Draw content edge to edge
//        WindowCompat.setDecorFitsSystemWindows(window,false)
        setContent {
//            WindowInsets.statusBars
            TempTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    AccountSetupView()
                    AppNavGraph(loginViewModel)
                }
            }
        }
    }
}
