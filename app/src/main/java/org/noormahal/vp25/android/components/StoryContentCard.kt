package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.StoryContentTextStyle
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun StoryContentCard(storyDetails: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp),
            text = storyDetails,
            style = StoryContentTextStyle,
            textAlign = TextAlign.Left
        )
    }
}


@Composable
fun ShimmerStory(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        repeat(5) {
            ShimmerLine(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                height = 18.dp
            )
        }
        ShimmerLine(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            height = 18.dp,
            widthFraction = 0.75f
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoryContentCardPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            StoryContentCard(
                storyDetails = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShimmerStoryPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ShimmerStory(paddingValues = PaddingValues(0.dp))
        }
    }
}
