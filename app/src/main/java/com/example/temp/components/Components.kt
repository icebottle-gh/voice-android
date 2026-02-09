package com.example.temp.components

import androidx.compose.runtime.Composable
import com.example.temp.presentation.ui.LoginInfo

private class Components : ComponentsApi {

    @Composable
    override fun Login(
        otpSent: Boolean,
        isEmail: Boolean,
        phoneCountryCode: String,
        onCountryCodeChange: (String) -> Unit,
        phone: String,
        onPhoneChange: (String) -> Unit,
        email: String,
        onEmailChange: (String) -> Unit,
        otp: String,
        onOtpChange: (String) -> Unit,
        onSendOTP: () -> Unit,
        onChangeNumber: () -> Unit,
        onChangeEmail: () -> Unit,
        onResendOTP: () -> Unit,
        onSubmit: () -> Unit
    ) {
        val loginInfo = LoginInfo(
            phone = phone,
            email = email,
            otp = otp,
            phoneCountryCode = phoneCountryCode
        )
        com.example.temp.components.Login(
            otpSent = otpSent,
            isEmail = isEmail,
            loginInfo = loginInfo,
            onCountryCodeChange = onCountryCodeChange,
            onPhoneChange = onPhoneChange,
            onEmailChange = onEmailChange,
            onOtpChange = onOtpChange,
            onSendOTP = onSendOTP,
            onChangeNumber = onChangeNumber,
            onChangeEmail = onChangeEmail,
            onResendOTP = onResendOTP,
            onSubmit = onSubmit
        )
    }
}

private val components: Components = Components()

