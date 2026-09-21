package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun StoriesTopBar(
    username: String,
    timePosted: String,
    storyCount: Int,
    storyIndex: Int,
    timeProgress: Float,
    onReportClick: () -> Unit = {},
    onMuteClick: () -> Unit = {},
    onBlockClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
    ) {
        VpStoriesProgressIndicator(storyCount, storyIndex, timeProgress)
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
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = timePosted, style = MaterialTheme.typography.labelSmall)
            }

            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = "Options"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Report") },
                        onClick = { expanded = false; onReportClick() }
                    )
                    DropdownMenuItem(
                        text = { Text("Mute") },
                        onClick = { expanded = false; onMuteClick() }
                    )
                    DropdownMenuItem(
                        text = { Text("Block") },
                        onClick = { expanded = false; onBlockClick() }
                    )
                }
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
