package org.noormahal.vp25.android.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = OrangeA200,
    onPrimary = OrangeBlack800,

    secondary = Orange400,
    onSecondary = OrangeBlack800,

//    tertiary = Pink80
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80

    background = OrangeBlack800,
    onBackground = Grey300,

    surface =  OrangeBlack800,
    onSurface = Grey300,

    primaryContainer = OrangeA200,
    onPrimaryContainer = White,
    secondaryContainer = OrangeBrown,
    onSecondaryContainer = White,

    surfaceVariant = OrangeBlack700,
    onSurfaceVariant = GreyA400,

//    outline = GreyA700,          // Borders, dividers
//    error = Orange700,
//    onError = OrangeBlack800
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeA200,
    secondary = GreyA700,
    tertiary = Orange400,

//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40,

    background = White,
    onBackground = OrangeBlack900,

    surface = White,
    onSurface = OrangeBlack900,

    surfaceVariant = Grey300,   // Outlined cards, filled text field background
    onSurfaceVariant = GreyA700,

    primaryContainer = Orange100,
    onPrimaryContainer = OrangeBlack800,
    secondaryContainer = Orange50,
    onSecondaryContainer = OrangeBlack800,

//    outline = GreyA400,         // Dividers, borders
//    error = Color(0xFFB00020),
//    onError = White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun VpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}