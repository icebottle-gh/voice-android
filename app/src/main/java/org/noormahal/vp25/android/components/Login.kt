package org.noormahal.vp25.android.components

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onCountryCodeChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSendOTP: () -> Unit,
    onOtpChange: (String) -> Unit,
    onChangeNumber: () -> Unit,
    onChangeEmail: () -> Unit,
    onResendOTP: () -> Unit,
    onSubmit: () -> Unit,
) {
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
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (!otpSent) {
            // ── Step 1: enter phone / email ───────────────────────────────
            Text(text = "Login / Register", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))

            if (!isEmail) {
                VpMobileNumberField(
                    mobileNumber = loginInfo.phone,
                    onMobileNumberChange = onPhoneChange,
                    onCountrySelected = { onCountryCodeChange(it.countryCode) },
                )
            } else {
                VpTextField(
                    value = loginInfo.email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = onSendOTP,
                shape = RoundedCornerShape(10.dp),
                enabled = isPhoneValid || isEmailValid,
            ) {
                Text(text = "Send OTP")
            }
        } else {
            // ── Step 2: enter OTP ─────────────────────────────────────────
            val sentTo = if (isEmail) loginInfo.email
                         else "${loginInfo.phoneCountryCode} ${loginInfo.phone}"

            Text(
                text = "Code sent to $sentTo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Change",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        if (isEmail) onChangeEmail() else onChangeNumber()
                    }
                )
                Text(
                    text = "Resend",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onResendOTP() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            VpOtpField(
                value = loginInfo.otp,
                onValueChange = onOtpChange,
                length = otpDigits,
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onSubmit,
                shape = RoundedCornerShape(10.dp),
                enabled = isOtpValid,
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPhonePreview() {
    Login(
        otpSent = false,
        isEmail = false,
        onCountryCodeChange = {},
        loginInfo = LoginInfo("", "", "", ""),
        onPhoneChange = {},
        onEmailChange = {},
        onSendOTP = {},
        onOtpChange = {},
        onChangeNumber = {},
        onChangeEmail = {},
        onResendOTP = {},
        onSubmit = {}
    )
}

@Composable
@Preview(showBackground = true)
fun LoginEmailPreview() {
    Login(
        otpSent = false,
        isEmail = true,
        loginInfo = LoginInfo("", "", "", ""),
        onCountryCodeChange = {},
        onPhoneChange = {},
        onEmailChange = {},
        onSendOTP = {},
        onOtpChange = {},
        onChangeNumber = {},
        onChangeEmail = {},
        onResendOTP = {},
        onSubmit = {}
    )
}

@Composable
@Preview(showBackground = true, apiLevel = 34)
fun LoginOtpPreview() {
    Login(
        otpSent = true,
        isEmail = false,
        loginInfo = LoginInfo("+91", "9876543210", "", "1234"),
        onCountryCodeChange = {},
        onPhoneChange = {},
        onEmailChange = {},
        onSendOTP = {},
        onOtpChange = {},
        onChangeNumber = {},
        onChangeEmail = {},
        onResendOTP = {},
        onSubmit = {}
    )
}
