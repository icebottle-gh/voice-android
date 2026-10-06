package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoriesTopBar(
    username: String,
    timePosted: String?,
    storyCount: Int?,
    storyIndex: Int?,
    timeProgress: Float?,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBarsIgnoringVisibility)
            .padding(horizontal = 2.dp, vertical = 0.dp)
    ) {
        // Nothing to show a progress bar for yet while the story data is still loading -
        // show a shimmer at the same height instead of made-up zero values, so the top
        // bar doesn't change height (and everything below it jump) once it loads.
        if (storyCount != null && storyIndex != null && timeProgress != null) {
            VpStoriesProgressIndicator(storyCount, storyIndex, timeProgress)
        } else {
            ShimmerBox(height = 4.dp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back"
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.heightIn(max = 30.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Shimmer placeholder while the current story's timestamp isn't known yet
                // (loading/error) - a blank line here would leave this row's height
                // intact but let the name above jump the instant real text appears.
                if (timePosted != null) {
                    Text(text = timePosted, style = MaterialTheme.typography.labelSmall)
                } else {
                    ShimmerLine(widthFraction = 0.4f, height = 12.dp)
                }
            }

            // TODO: wire up report/mute/block once those features exist
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_more_vert_24),
                    contentDescription = "Options"
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoriesTopBarPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            StoriesTopBar(
                username = "Sajidha Abdulla",
                timePosted = "Yesterday, 10:45 pm",
                storyCount = 5,
                storyIndex = 3,
                timeProgress = 0.2f
            )
        }
    }
}

// Loading state: story data hasn't arrived yet, so there's nothing to show a progress bar for.
@Preview(name = "Loading - Light", showBackground = true)
@Preview(name = "Loading - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoriesTopBarLoadingPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            StoriesTopBar(
                username = "Sajidha Abdulla",
                timePosted = null,
                storyCount = null,
                storyIndex = null,
                timeProgress = null
            )
        }
    }
}
