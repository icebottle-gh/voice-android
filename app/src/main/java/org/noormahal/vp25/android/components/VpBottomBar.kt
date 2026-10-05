package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.navigation.screensWithBottom
import org.noormahal.vp25.android.theme.VpTheme

/**
 * NavigationBarItem hardcodes rememberRipple() rather than reading LocalIndication, so the
 * ripple can only be suppressed via RippleTheme. Scoped locally so back buttons/other ripples
 * elsewhere in the app are unaffected.
 */
private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0f, 0f, 0f, 0f)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VpBottomBar(
    currentScreen: Screen,
    currentRoute: String?,
    items: List<Screen.BottomScreen>,
    onBottomScreenClick: (Screen) -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if(currentScreen in items){
        val topOutlineColor = MaterialTheme.colorScheme.outline
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp,
            // Extra horizontal inset on top of the default system-bar insets, so the items
            // sit closer together instead of spreading edge to edge - the bar's own
            // background stays full width, only the item row is padded in.
            windowInsets = NavigationBarDefaults.windowInsets.add(WindowInsets(left = 24.dp, right = 24.dp)),
            modifier = Modifier
                .drawBehind {
                drawLine(
                    color = topOutlineColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = .5.dp.toPx()
                )
            }
        ) {
            items.forEach{
                bottomScreen->
                val isSelected = currentRoute == bottomScreen.bottomRoute
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onBottomScreenClick(bottomScreen)
                    },
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
                    label = {
                        Text(
                            text = bottomScreen.bottomTitle,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                )
            }
        }
        }
    }
}
