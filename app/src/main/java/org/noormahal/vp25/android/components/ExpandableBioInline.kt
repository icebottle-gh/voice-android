package org.noormahal.vp25.android.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableBioInline(bio: String) {
    var expanded by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    )
    val actionText = if (!expanded) "Read more" else ""
    val maxChars = 150

    val displayText = bio
    val shouldTruncate = !expanded && displayText.length > maxChars

    val visibleText = if (shouldTruncate) {
        displayText.take(maxChars) + "..."
    } else {
        displayText
    }

    val annotatedString = buildAnnotatedString {
        append(visibleText)
        if (shouldTruncate) {
            val start = length
            append(actionText)
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontStyle = FontStyle.Italic,
                ),
                start = start,
                end = start + actionText.length
            )
            addStringAnnotation(
                tag = "read_more",
                annotation = actionText,
                start = start,
                end = start + actionText.length
            )
        }
    }

    ClickableText(
        text = annotatedString,
        style = textStyle,
        onClick = { offset ->
            val annotations = annotatedString.getStringAnnotations("read_more", offset, offset)
            if (annotations.isNotEmpty()) {
                expanded = !expanded
            }
        }
    )
}
