package com.example.temp.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.R
import com.example.temp.presentation.ui.LoginInfo

@Composable
fun LoginOtp(
    loginInfo:LoginInfo,
    onOtpChange:()->Unit,
    onChangeNumber:()->Unit,
    onChangeEmail:()->Unit,
    onResendOTP:()->Unit,
    onSubmit:()->Unit,
){
    val otpDigits = 6
    val isOtpValid = loginInfo.otp.length == otpDigits && loginInfo.otp.all { it.isDigit() }
    
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
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Login / Register", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        //mobile no. disabled.
        OutlinedTextField(
            value = loginInfo.phone,
            enabled = false,
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text(text = "Mobile No.") }
        )
        //email disabled
        OutlinedTextField(
            value = loginInfo.email,
            onValueChange = {},
            singleLine = true,
            enabled = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text(text = "Email") }
        )
//        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = loginInfo.otp,
            onValueChange = { otp->
                val filteredValue = otp.filter { it.isDigit() }
                if (filteredValue.length <= otpDigits)
                    onOtpChange
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "OTP") },
            isError = loginInfo.otp.isNotEmpty() && !isOtpValid
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                //                        viewModel.login(phone,otp)
                onSubmit
//                    onLoginSuccess()
//                        navController.navigate(Screen.BottomScreen.Stories.bottomRoute){
//                            // This pops up to the start destination of the graph to
//                            // avoid building up a large stack of destinations
//                            // on the back stack as users select items
//                            popUpTo(navController.graph.startDestinationId) {
//                                inclusive = true
//                            }
//                        }
            },
            shape = RoundedCornerShape(10.dp),
            enabled = isOtpValid
        ) {
            Text(text = "Submit")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.clickable { onResendOTP },
            text = "Resend OTP?",
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.clickable { onChangeNumber },
            text = "Change Number",
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.clickable { onChangeEmail },
            text = "Change Email",
            color = Color.Blue
        )
    }

    }


@Composable
@Preview(showBackground = true)
fun LoginotpPreview(){
    LoginOtp(
        loginInfo = LoginInfo("12345566642","halo@gmial.com",""),
        onOtpChange = {},
        onSubmit = { /*TODO*/ },
        onChangeNumber = { /*TODO*/ },
        onResendOTP = { },
        onChangeEmail = {}
    )
}