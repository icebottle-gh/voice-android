package com.example.temp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.temp.presentation.navigation.Screen
import com.example.temp.presentation.navigation.allScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _currentScreen = MutableStateFlow<Screen>(Screen.BottomScreen.Stories)
    val currentScreen = _currentScreen.asStateFlow()

    //mutablestate to be used Inside Composables (not ViewModels). Works for simple UI state like text fields.
//    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Stories)
//    val currentScreen: MutableState<Screen>
//        get() = _currentScreen

    fun setCurrentScreen(route: String?){
        _currentScreen.value = allScreens.find {
            it.route == route
        }?: Screen.BottomScreen.Stories
    }
}