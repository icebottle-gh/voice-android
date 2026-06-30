package org.noormahal.vp25.android.components

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.presentation.ui.LoginInfo

@Composable
fun Login(
    otpSent: Boolean,
    isEmail: Boolean,
    loginInfo: LoginInfo,
    onCountryCodeChange:(String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSendOTP: () -> Unit,
    onOtpChange:(String)->Unit,
    onChangeNumber:()->Unit,
    onChangeEmail:()->Unit,
    onResendOTP:()->Unit,
    onSubmit:()->Unit,
){
    val focusManager = LocalFocusManager.current

    val phoneDigitsRange = 7..15
    val isPhoneValid = loginInfo.phone.length in phoneDigitsRange && loginInfo.phone.all { it.isDigit() }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(loginInfo.email).matches() &&
            loginInfo.email.substringAfterLast(".", "").length >= 2

    val otpDigits = 6
    val isOtpValid = loginInfo.otp.length == otpDigits && loginInfo.otp.all { it.isDigit() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
            .focusable(false),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.status),
            contentDescription = "Logo",
            modifier =  Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Login / Register", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (!isEmail){
            VpMobileNumberField(
                mobileNumber = loginInfo.phone,
                onMobileNumberChange = onPhoneChange,
                onCountrySelected = { onCountryCodeChange(it.countryCode) },
                enabled = !otpSent,
            )
        }else{
            VpTextField(
                value = loginInfo.email,
                onValueChange = onEmailChange,
                label = "Email",
                keyboardType = KeyboardType.Email,
                enabled = !otpSent,
            )
        }


//        //mobile
//        OutlinedTextField(
//            value = loginInfo.phone,
//            enabled = !otpSent,
//            onValueChange = onPhoneChange,
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//            label = { Text(text = "Mobile No.") },
//            shape = RoundedCornerShape(10.dp),
//            isError = loginInfo.phone.isNotEmpty() && !isPhoneValid
//        )

        if(otpSent){
            VpTextField(
                value = loginInfo.otp,
                onValueChange = onOtpChange,
                label = "OTP",
                keyboardType = KeyboardType.Number,
                digitOnly = true,
                maxLength = otpDigits,
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        //send otp button
        Button(
            onClick = {
                if (!otpSent)
                    onSendOTP()
                else
                    onSubmit()
////                    println("Button clicked" )
//                    loginViewModel.requestOtp(phone)
//                    otpSent = true
            },
            shape = RoundedCornerShape(10.dp),
            enabled = if (!otpSent) isPhoneValid or isEmailValid else isOtpValid
        ) {
            Text(text = if (!otpSent) "Send OTP" else "Submit")
        }

        if (otpSent){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.clickable { onResendOTP() },
                text = "Resend OTP?",
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier.clickable { onChangeNumber() },
                text = "Change Number",
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier.clickable { onChangeEmail() },
                text = "Change Email",
                color = Color.Blue
            )
        }

    }
}

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
@Preview(showBackground = true, apiLevel = 34)
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
