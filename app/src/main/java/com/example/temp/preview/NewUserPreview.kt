package com.example.temp.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.temp.components.Login
import com.example.temp.presentation.ui.AccountSetupView
import com.example.temp.presentation.ui.LoginInfo

@Composable
@Preview(showBackground = true)
fun LoginPhonePreview(){
    Login(
        otpSent=false,
        isEmail = false,
        onCountryCodeChange = {},
        loginInfo = LoginInfo("","", "", ""),
        onPhoneChange = {},
        onEmailChange= {},
        onSendOTP= {},
        onOtpChange= {},
        onChangeNumber= {},
        onChangeEmail= {},
        onResendOTP= {},
        onSubmit = {}
    )
}

@Composable
@Preview(showBackground = true)
fun LoginEmailPreview(){
    Login(
        otpSent=false,
        isEmail = true,
        loginInfo = LoginInfo("","", "", ""),
        onCountryCodeChange = {},
        onPhoneChange = {},
        onEmailChange= {},
        onSendOTP= {},
        onOtpChange= {},
        onChangeNumber= {},
        onChangeEmail= {},
        onResendOTP= {},
        onSubmit = {}
    )
}

@Composable
@Preview(showBackground = true, apiLevel = 35)
fun LoginOtpPreview(){
    Login(
        otpSent=true,
        isEmail = false,
        loginInfo = LoginInfo("","123544651", "", ""),
        onCountryCodeChange = {},
        onPhoneChange = {},
        onEmailChange= {},
        onSendOTP= {},
        onOtpChange= {},
        onChangeNumber= {},
        onChangeEmail= {},
        onResendOTP= {},
        onSubmit = {}
    )
}

@Composable
@Preview(showBackground = true, apiLevel = 35)
fun AccountSetupPreview(){
    AccountSetupView {

    }
}