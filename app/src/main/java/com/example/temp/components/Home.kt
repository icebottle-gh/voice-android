package com.example.temp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.temp.R
import com.example.temp.presentation.navigation.HomeNavGraph
import com.example.temp.presentation.navigation.Screen
import com.example.temp.presentation.navigation.screensWithBottom
import com.example.temp.presentation.ui.AppBarView

@Composable
fun Home(
    currentRoute: String,
    content: @Composable (modifier: Modifier) -> Unit = {},
    topBarAction: String = "menu",
    onNavigate: (String) -> Unit = {},
) {
    val currentScreen = Screen.BottomScreen.fromRoute(currentRoute)
    val title = currentScreen.title

    val floatingButton : @Composable () -> Unit  = if (currentScreen == Screen.BottomScreen.Stories) {-> NewStoryFloatingButton()} else {-> {}}

    Scaffold(
//        modifier = Modifier.safeDrawingPadding(),
        bottomBar = {
            BottomBarView(
//                viewModel = viewModel,
                currentRoute = currentRoute,
            )
        },
        topBar = {
            AppBarView(
                currentScreen = currentScreen,
                topBarAction = topBarAction,
                title = title,
            )
        },
        floatingActionButton = floatingButton,
        contentWindowInsets = WindowInsets.safeDrawing
    ){
        content(Modifier.padding(it))
    }
}


@Composable
fun NewStoryFloatingButton(): Unit {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_create_24),
            contentDescription = "New Story"
        )
    }
}


@Composable
fun BottomBarView(
    currentRoute: String?,
    onNavigate: (String) -> Unit = {},
){
    NavigationBar {
        screensWithBottom.forEach{
                item->
            val isSelected = currentRoute == item.bottomRoute
//                val tint = if(isSelected) Color.White else Color.Black
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onNavigate(item.bottomRoute)
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
                label = { Text(text = item.bottomTitle) }
//                            selectedContentColor = Color.White,
//                            unselectedContentColor = Color.Black

            )
        }

    }
}
