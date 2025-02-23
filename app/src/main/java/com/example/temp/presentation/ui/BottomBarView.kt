package com.example.temp.presentation.ui

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.temp.presentation.navigation.Screen
import com.example.temp.presentation.navigation.screensInBottom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarView(
    currentScreen: Screen,
//    viewModel: MainViewModel,
    currentRoute: String?,
    controller: NavController
){
//    val currentScreen by viewModel.currentScreen.collectAsState()

    if(currentScreen in screensInBottom){
        NavigationBar {
            screensInBottom.forEach{
                item->
                val isSelected = currentRoute == item.bottomRoute
//                val tint = if(isSelected) Color.White else Color.Black
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                              controller.navigate(item.bottomRoute)
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if(item.badgeCount!=null){
                                    Badge(modifier = Modifier.offset(x=(-4).dp, y=8.dp)){
                                        Text(text = item.badgeCount.toString())
                                    }
                                }else if(item.hasNews){
                                    Badge(modifier =  Modifier.offset(x=(-2.dp)))
                                }
                            }

                        ) {
                            Icon(

//                            tint=tint,
                                painter = if (currentRoute==item.bottomRoute){
                                    painterResource(id = item.selectedIcon)
                                }else{
                                    painterResource(id = item.unselectedIcon)
                                },
                                contentDescription = item.title,

////                            title.value = item.bottomTitle
                            )
                        }

                    },
                    label = { Text(text = item.bottomTitle)}
//                            selectedContentColor = Color.White,
//                            unselectedContentColor = Color.Black

                )
            }

        }
    }
}
