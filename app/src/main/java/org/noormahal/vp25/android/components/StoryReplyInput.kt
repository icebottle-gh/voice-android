package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

private val REPLY_PILL_SHAPE = RoundedCornerShape(percent = 50)

/**
 * Plain reply pill for someone else's story - no leading/trailing icon, just text.
 * Eventually this should likely grow into something closer to [MessageInput] (emoji,
 * voice replies) once reply-to-story has backend support; kept minimal until then.
 */
@Composable
fun StoryReplyInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 12.dp),
        shape = REPLY_PILL_SHAPE,
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            if (value.isEmpty()) {
                Text(
                    text = "Reply",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 412)
@Preview(name = "Dark", showBackground = true, widthDp = 412, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoryReplyInputPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            var value by remember { mutableStateOf("") }
            StoryReplyInput(value = value, onValueChange = { value = it })
        }
    }
}
