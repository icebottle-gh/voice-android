package com.example.temp.presentation.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSetupView(onSubmit: () -> Unit) {
    val spacerHeight = 20.dp

    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    val genderOptions = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") } // Initial selection


    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())}

    var dob by remember { mutableStateOf("") }

    val showDatePicker = {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                dob = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(20.dp)
            .imePadding()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Account Setup",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(40.dp))

        //MOBILE
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            enabled = false,
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text(text = "Mobile") }
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        //FULL NAME
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = fullName,
            enabled = true,
            onValueChange = {fullName = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Full Name") }
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        //DOB
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    println("in pointer input")
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showDatePicker()
                        }
                    }

                },
            value = dob,
            enabled = true,
            onValueChange = {},
            singleLine = true,
            readOnly = true,
            label = { Text(text = "Date Of Birth") }
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        //GENDER DROPDOWN
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedGender,
                onValueChange = { }, // Read-only for dropdown
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            selectedGender = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(spacerHeight))

//        //EMAIL
//        TextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = "",
//            enabled = true,
//            onValueChange = {},
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//            label = { Text(text = "Email") }
//        )
//        Spacer(modifier = Modifier.height(spacerHeight))
//
//        //ALTERNATE MOBILE
//        TextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = "",
//            enabled = true,
//            onValueChange = {},
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//            label = { Text(text = "Alternate Mobile") }
//        )
//        Spacer(modifier = Modifier.height(spacerHeight))

        //BIO
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = bio,
            enabled = true,
            onValueChange = {bio=it},
            minLines = 3,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Bio") }
        )
        Spacer(modifier = Modifier.height(30.dp))


        Button(
            onClick = {
                      //TODO Go to Home Screen, if info is valid.
                      onSubmit()
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Submit")
        }
    }

}

@Composable
@Preview
fun AccountSetupPreview(){
    AccountSetupView {

    }
}