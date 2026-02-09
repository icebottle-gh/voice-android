package com.example.temp.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.temp.components.ActionCentreScreenPreview
import com.example.temp.components.Actionable
import com.example.temp.components.ActionablePageSectionPreview
import com.example.temp.components.Home
import com.example.temp.presentation.navigation.Screen
import com.example.temp.presentation.ui.HomeView

@Preview
@Composable
fun ActionCentreHomePreview() {
    Home(
        currentRoute = Screen.BottomScreen.Notifications.bottomRoute,
        content = { ActionCentreScreenPreview(modifier = it, showActive = true)}
    )
}

@Preview
@Composable
fun ActionCentreHistoryHomePreview() {
    Home(
        currentRoute = Screen.BottomScreen.Notifications.bottomRoute,
        content = { ActionCentreScreenPreview(modifier = it, showActive = false)}
    )
}

@Preview
@Composable
fun ActionablePreview() {
    Home(
        currentRoute = Screen.BottomScreen.Notifications.bottomRoute,
        topBarAction = "back",
        content = {ActionablePageSectionPreview(modifier = it)}
    )
}