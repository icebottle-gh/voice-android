package org.noormahal.vp25.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.noormahal.vp25.android.R

// Bundled rather than FontFamily.Default - the system font (and whether it even has an
// italic variant, or synthesizes one when it doesn't) varies by device/OEM skin/emulator
// image, which is exactly why italic silently failed to render on some devices while
// working in the @Preview pane. Bundling guarantees the same four faces everywhere.
val RobotoStoryFontFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.roboto_bold_italic, FontWeight.Bold, FontStyle.Italic)
)

val Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 25.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// Long-form reading text for story content — intentionally larger/roomier than bodyLarge
val StoryContentTextStyle = TextStyle(
    fontFamily = RobotoStoryFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.5.sp
)
