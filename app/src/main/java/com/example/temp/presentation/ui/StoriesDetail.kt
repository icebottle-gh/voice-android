package com.example.temp.presentation.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StoriesDetail(postId: String){
    Text(text = "Story Details", fontSize = 24.sp)
    Spacer(modifier = Modifier.padding(8.dp))
    Text(text = "Post ID: $postId", fontSize = 18.sp)
}