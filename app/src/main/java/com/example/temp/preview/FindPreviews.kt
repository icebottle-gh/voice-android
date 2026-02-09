package com.example.temp.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.temp.components.FindPersonPreview
import com.example.temp.components.Home
import com.example.temp.components.PersonProfileLongBioPreview
import com.example.temp.presentation.navigation.Screen

@Preview
@Composable
fun FindPersonHomePreview() {
    Home(
        currentRoute = Screen.BottomScreen.Find.bottomRoute,
        content = { FindPersonPreview(modifier = it)}
    )
}

@Preview
@Composable
fun FindPersonProfilereview() {
    Home(
        currentRoute = Screen.BottomScreen.Find.bottomRoute,
        topBarAction = "back",
        content = { PersonProfileLongBioPreview(modifier = it) }
    )
}