package com.example.temp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.R
import com.example.temp.presentation.ui.LoginInfo

@Composable
fun LoginPhoneEmail(
    loginInfo: LoginInfo,
    onPhoneChange: ()->Unit,
    onEmailChange: ()->Unit,
    onSendOTP: ()->Unit,
){

    val phoneDigitsRange = 7..15
    val isPhoneValid = loginInfo.phone.length in phoneDigitsRange && loginInfo.phone.all { it.isDigit() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
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

        //mobile
        OutlinedTextField(
            value = loginInfo.phone,
            onValueChange = {onPhoneChange},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text(text = "Mobile No.") },
            isError = loginInfo.phone.isNotEmpty() && !isPhoneValid
        )
        //email
        OutlinedTextField(
            value = loginInfo.email,
            onValueChange = {onEmailChange},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text(text = "Email") },
            isError = loginInfo.email.isNotEmpty() && !isPhoneValid
        )
        Spacer(modifier = Modifier.height(15.dp))
        //send otp button
        Button(
            onClick = {
                      onSendOTP
////                    println("Button clicked" )
//                    loginViewModel.requestOtp(phone)
//                    otpSent = true
            },
            shape = RoundedCornerShape(10.dp),
            enabled = isPhoneValid
        ) {
            Text(text = "Send OTP")
        }

    }
}

@Composable
@Preview(showBackground = true)
fun LoginemailPreview(){
    LoginPhoneEmail(
        loginInfo = LoginInfo("", "", ""),
        onPhoneChange = { /*TODO*/ },
        onEmailChange = { /*TODO*/ }) {
        
    }
}
