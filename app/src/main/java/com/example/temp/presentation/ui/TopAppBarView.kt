package com.example.temp.presentation.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.R
import com.example.temp.presentation.navigation.Screen
import com.example.temp.presentation.navigation.screensWithBottom


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AppBarView(
    title:String,
    onBackNavClicked:()->Unit={},
    currentScreen: Screen)
{
    if(currentScreen in screensWithBottom){
        var showDropDownMenu by remember { mutableStateOf(false) }

        //so as to control visibility based on diff situations BACK OR DRAWER
        val navigationIcon: (@Composable () -> Unit) =
            if(!title.contains(stringResource(id = R.string.app_title))) {
                {

                    IconButton(onClick = { onBackNavClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            //TODO:color explicit here, change to be theme friendly
//                        tint = Color.Black,
                            contentDescription = null
                        )
                    }

                }
            }
            else {
                {
                    IconButton(
                        onClick = {
                            //Open the drawer - its a suspend function remember
                            //TODO: side drawer
                        }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                    }
                }
            }


        TopAppBar(
            windowInsets = WindowInsets.statusBarsIgnoringVisibility,
//            modifier = Modifier.safeContentPadding(),
            title= {
                Text(
                    text = title,
                    // TODO:color explicit here, change to be theme friendly
//                color = colorResource(id = R.color.black),
                    fontSize = 20.sp,
                    modifier = Modifier.heightIn(max = 30.dp)
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        showDropDownMenu = true
                    }
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription =null )
                }

                MaterialTheme (shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(8.dp))){
                    DropdownMenu(
                        expanded = showDropDownMenu,
                        onDismissRequest = { showDropDownMenu=false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Drop down item") },
                            onClick = { /*TODO*/ }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Drop down item") },
                            onClick = { /*TODO*/ }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Drop down item") },
                            onClick = { /*TODO*/ }
                        )
                    }
                }


            },
            navigationIcon = navigationIcon

        )
    }


}


@Preview
@Composable
fun AppBarViewPreview(){
    AppBarView(title = "Voice", currentScreen = Screen.BottomScreen.Stories)
}