package org.noormahal.vp25.android.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun NormalButton(text:String){
    Button(
        onClick = { },
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = text)
    }
}