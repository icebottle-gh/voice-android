package com.example.temp.components

import androidx.compose.runtime.Composable
import com.example.temp.presentation.ui.LoginInfo


@Composable
fun LoginComponent(
    otpSent: Boolean,
    isEmail: Boolean,
    phoneCountryCode: String,
    onCountryCodeChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    otp: String,
    onOtpChange: (String)->Unit,
    onSendOTP: () -> Unit,
    onChangeNumber: ()-> Unit,
    onChangeEmail: ()-> Unit,
    onResendOTP: ()-> Unit,
    onSubmit: () -> Unit,
) {

}


