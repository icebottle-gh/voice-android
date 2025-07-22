package com.example.temp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.R
import com.example.temp.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginView(onLoginSuccess: () -> Unit, viewModel: LoginViewModel) {
    val scope = rememberCoroutineScope()
    val otp by viewModel.otp
    var phone by remember { mutableStateOf("") }
//    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) } //controls flow
    val otpDigits = 4
    val phoneDigits = 10

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
        if (!otpSent){
            OutlinedTextField(
                value = phone,
                onValueChange = {phone = it},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                label = { Text(text = "Mobile No.")}
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    scope.launch {
                        viewModel.requestOtp(phone) {
                            otpSent = true
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Send OTP")
            }
        }else{
            OutlinedTextField(
                value = phone,
                enabled = false,
                onValueChange = {phone = it},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                label = { Text(text = "Mobile No.")}
            )
//            Text(
//                text = "Edit",
//                modifier = Modifier.clickable { otpSent = false },
//                color = Color.Magenta
//            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = otp,
                onValueChange = {
//                    val filteredValue = it.filter { it.isDigit() }
//                    if (filteredValue.length <= otpDigits)
//                        otp = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "OTP")}
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    if (otp.length >= otpDigits){
                        viewModel.login(phone, otp) { onLoginSuccess() }
                    }
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Submit")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(modifier = Modifier.clickable {  }, text = "Resend OTP?",color = Color.Blue)
            Spacer(modifier = Modifier.height(5.dp))
            Text(modifier = Modifier.clickable { otpSent=false }, text = "Change Number",color = Color.Blue)
        }


    }


}

//@Preview
//@Composable
//fun LoginPreview(){
//    LoginView({}, viewModel = LoginViewModel())
//}
