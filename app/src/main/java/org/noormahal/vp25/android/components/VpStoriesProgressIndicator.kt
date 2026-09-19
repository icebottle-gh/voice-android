package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun VpStoriesProgressIndicator(
    storyCount: Int,
    storyIndex: Int,
    timeProgress: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(storyCount) { index ->
            val indicatorProgress = when {
                index < storyIndex -> 1f
                index == storyIndex -> timeProgress
                else -> 0f
            }
            LinearProgressIndicator(
                progress = { indicatorProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50)),
                color = MaterialTheme.colorScheme.onSurface,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpStoriesProgressIndicatorPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VpStoriesProgressIndicator(
                storyCount = 5,
                storyIndex = 1,
                timeProgress = 0.4f,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
