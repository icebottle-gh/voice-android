package com.example.temp

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.temp.presentation.navigation.AppNavGraph
import com.example.temp.presentation.viewmodel.LoginViewModel
import com.example.temp.ui.theme.TempTheme

class MainActivity : ComponentActivity() {
    val loginViewModel by viewModels<LoginViewModel>()
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
