package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

private const val SELECTION_OVERLAY_ALPHA = 0.3f

fun Modifier.selectionOverlay(selected: Boolean): Modifier = composed {
    if (selected) {
        background(color = MaterialTheme.colorScheme.primary.copy(alpha = SELECTION_OVERLAY_ALPHA))
    } else {
        this
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectionOverlayPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .selectionOverlay(selected = true)
            )
        }
    }
}
