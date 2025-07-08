package com.example.temp.presentation.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSetupView(){
    val spacerHeight = 20.dp

    val genderOptions = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(genderOptions[0]) } // Initial selection

    val context = LocalContext.current
    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
    var dobText by remember { mutableStateOf("") }
    // Used to open the DatePickerDialog
    val calendar = Calendar.getInstance()
//    val datePickerDialog = DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//                calendar.set(year, month, dayOfMonth)
//                dobText = dateFormatter.format(calendar.time)
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        ).apply {
//            datePicker.maxDate = System.currentTimeMillis() // Optional: prevent future dates
//        }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
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
        TextField(
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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            enabled = true,
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Full Name") }
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        //DOB
        TextField(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    println("clicked dob")
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            dobText = dateFormatter.format(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.maxDate = System.currentTimeMillis()
                    }
                    datePickerDialog.show()
                },
            value = dobText,
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
            TextField(
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                value = selectedOptionText,
                onValueChange = { }, // Read-only for dropdown
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(spacerHeight))

        //EMAIL
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            enabled = true,
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text(text = "Email") }
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        //ALTERNATE MOBILE
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            enabled = true,
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text(text = "Alternate Mobile") }
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        //BIO
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            enabled = true,
            onValueChange = {},
            minLines = 3,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Bio") }
        )
        Spacer(modifier = Modifier.height(30.dp))


        Button(
            onClick = {
                      //TODO
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
    AccountSetupView()
}