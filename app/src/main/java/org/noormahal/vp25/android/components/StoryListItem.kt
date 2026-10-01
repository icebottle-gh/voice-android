package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun StoryListItem(
    user: User,
    isMyStory: Boolean = false,
    onAddStoryClick: () -> Unit = {},
    onClick: () -> Unit
) {
    val cardTitle = if (isMyStory) {
        if (user.hasUnviewedStory) "My Story" else "Add Story"
    } else {
        user.displayName
    }

    val borderColor = if (isMyStory || user.hasUnviewedStory)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .then(
                if (isMyStory && !user.hasUnviewedStory) {
                    Modifier.drawBehind {
                        val strokeWidth = 3.dp.toPx()
                        val dashLength = 10f
                        val gapLength = 10f
                        val pathEffect =
                            PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
                        drawRoundRect(
                            color = borderColor,
                            style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                } else {
                    Modifier.border(
                        width = 1.5.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = borderColor
                    )
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO: profile image/avatar once user.userImageThumb is available
                Text(
                    text = cardTitle,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                if (isMyStory)
                    IconButton(
                        onClick = onAddStoryClick,
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_post_add_24),
                            contentDescription = "Add Story",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
            }
        }
    }
}

@Composable
fun StoryListItemSkeleton() {
    // The whole card shape shimmers, rather than a bordered card with a shimmering
    // name inside it - at this point we don't know yet whether this slot resolves
    // to an item at all, so it shouldn't look like a real card is already there.
    ShimmerBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 4.dp),
        height = 64.dp,
        shape = RoundedCornerShape(10.dp)
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoryListItemPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                StoryListItem(User("234", "Sajidha Abdulla", true), isMyStory = true) {}
                StoryListItem(User("234", "Muhammed Salih", true), isMyStory = false) {}
                // Viewed story -> outlineVariant ring
                StoryListItem(User("234", "John Doe", false), isMyStory = false) {}

                // Loading
                StoryListItemSkeleton()
                StoryListItemSkeleton()
                StoryListItemSkeleton()
            }
        }
    }
}
