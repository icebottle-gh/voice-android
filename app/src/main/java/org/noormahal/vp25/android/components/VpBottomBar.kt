package org.noormahal.vp25.android.components

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
import org.noormahal.vp25.android.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VpBottomBar(
    currentScreen: Screen,
    currentRoute: String?,
    items: List<Screen.BottomScreen>,
    onBottomScreenClick: (Screen) -> Unit
){

    if(currentScreen in items){
        NavigationBar {
            items.forEach{
                bottomScreen->
                val isSelected = currentRoute == bottomScreen.bottomRoute
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onBottomScreenClick(bottomScreen) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if(bottomScreen.badgeCount!=null){
                                    Badge(modifier = Modifier.offset(x=(-4).dp, y=8.dp)){
                                        Text(text = bottomScreen.badgeCount.toString())
                                    }
                                }else if(bottomScreen.hasNews){
                                    Badge(modifier =  Modifier.offset(x=(-2.dp)))
                                }
                            }

                        ) {
                            Icon(
                                painter = if (currentRoute==bottomScreen.bottomRoute){
                                    painterResource(id = bottomScreen.selectedIcon)
                                }else{
                                    painterResource(id = bottomScreen.unselectedIcon)
                                },
                                contentDescription = bottomScreen.title,
                            )
                        }

                    },
                    label = { Text(text = bottomScreen.bottomTitle)}

                )
            }
        }
    }
}
