package com.example.temp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.temp.components.Login
import com.example.temp.presentation.viewmodel.LoginViewModel


@Composable
fun LoginView(onLoginSuccess: () -> Unit, loginViewModel: LoginViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val otp by loginViewModel.otp
    var mobile by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var otpSent by remember {
        mutableStateOf(false)
    }
    var phoneCountryCode by remember {
        mutableStateOf("")
    }


    Login(
        otpSent = otpSent,
        isEmail = true,
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
            loginViewModel.requestOtp(mobile=mobile)
            otpSent=true
        },
        onOtpChange= {
            // TODO typing otp
        },
        onChangeNumber= {
            otpSent = false
        },
        onChangeEmail= {
            otpSent = false
        },
        onResendOTP= {
             loginViewModel.requestOtp(phoneCountryCode+mobile)
        },
    ) {
        //OnSubmit
        onLoginSuccess()
    }


}

data class LoginInfo(
    val phoneCountryCode:String,
    val phone:String,
    val email:String,
    val otp:String,
)
