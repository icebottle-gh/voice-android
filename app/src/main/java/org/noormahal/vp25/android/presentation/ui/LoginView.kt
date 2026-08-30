package org.noormahal.vp25.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.noormahal.vp25.android.components.Login
import org.noormahal.vp25.android.presentation.viewmodel.LoginViewModel


@Composable
fun LoginView(onLoginSuccess: () -> Unit, loginViewModel: LoginViewModel = viewModel()) {
    val peekedOtp by loginViewModel.otp
    val loginError by loginViewModel.loginError
    var mobile by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var otp by remember {
        mutableStateOf("")
    }
    var otpSent by remember {
        mutableStateOf(false)
    }
    var phoneCountryCode by remember {
        mutableStateOf("+91")
    }

    // Mobile is the intended login identifier (email is UI-only for now, not wired up).
    val isEmail = false
    val identifier = phoneCountryCode + mobile

    LaunchedEffect(peekedOtp) {
        if (peekedOtp.isNotEmpty()) otp = peekedOtp
    }

    Login(
        otpSent = otpSent,
        isEmail = isEmail,
        loginInfo = LoginInfo(
            phoneCountryCode,
            mobile,
            email,
            otp
        ),
        onCountryCodeChange = {phoneCountryCode = it},
        onPhoneChange = { mobile = it },
        onEmailChange= { email = it },
        onSendOTP= {
            loginViewModel.requestOtp(identifier)
            otpSent=true
        },
        onOtpChange= { otp = it },
        onChangeNumber= {
            otpSent = false
            otp = ""
        },
        onChangeEmail= {
            otpSent = false
            otp = ""
        },
        onResendOTP= {
             loginViewModel.requestOtp(identifier)
        },
        errorMessage = loginError,
    ) {
        loginViewModel.login(identifier, otp, onLoginSuccess)
    }


}

data class LoginInfo(
    val phoneCountryCode:String,
    val phone:String,
    val email:String,
    val otp:String,
)
