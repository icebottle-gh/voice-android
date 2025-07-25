package com.example.temp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.temp.components.LoginOtp
import com.example.temp.components.LoginPhoneEmail
import com.example.temp.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginView(onLoginSuccess: () -> Unit, loginViewModel: LoginViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val otp by loginViewModel.otp
    var phone by remember { mutableStateOf("") }
    var email by remember {
        mutableStateOf("")
    }
    var otpSent by remember { mutableStateOf(false) } //controls flow

    if (!otpSent){
        LoginPhoneEmail(
            loginInfo = LoginInfo(
                phone,
                email,
                otp
            ),
            onPhoneChange = { /*TODO*/ },
            onEmailChange = { /*TODO*/ }
        ) {
            scope.launch {
                loginViewModel.requestOtp(phone) {
                    otpSent = true
                }
            }
        }
    }else{
        LoginOtp(
            loginInfo = LoginInfo(
                phone,
                email,
                otp
            ),
            onOtpChange = { /*TODO*/ },
            onChangeNumber = { /*TODO*/ },
            onResendOTP = { /*TODO*/ },
            onChangeEmail = { /*TODO*/ }
        ) {
            loginViewModel.login(phone, otp) { onLoginSuccess() }
        }
    }


}

data class LoginInfo(
    val phone:String,
    val email:String,
    val otp:String,
)
@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    LoginView({}, loginViewModel = LoginViewModel())
}
