package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.theme.VpTheme

private const val LOADING_SKELETON_COUNT = 10

@Composable
fun StoriesList(
    myStoryUser: User,
    users: List<User>,
    onMyStoryClick: () -> Unit,
    onUserClick: (User) -> Unit,
    onAddStoryClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 22.dp)
    ) {
//        item {
//            VpListTitle(title = "Stories")
//        }

        if (isLoading) {
            // We don't know yet whether anyone (including the current user) has a
            // story, so every slot is a generic skeleton - not just the first one.
            items(LOADING_SKELETON_COUNT) {
                StoryListItemSkeleton()
            }
        } else {
            //Your Story card here
            item {
                StoryListItem(
                    user = myStoryUser,
                    isMyStory = true,
                    onAddStoryClick = onAddStoryClick,
                    onClick = onMyStoryClick
                )
            }

            //other users stories card
            items(users) { user ->
                StoryListItem(user = user, onClick = { onUserClick(user) })
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoriesListPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            StoriesList(
                myStoryUser = User("123", "Sajidha", hasUnviewedStory = false),
                users = listOf(
                    User("Saji", "Sajidha Abdulla", true),
                    User("sali", "Muhammed Salih", true),
                    User("hahi", "Ravi", true),
                    User("kiki", "Ahmed", false),
                    User("chuchu", "Dani", false),
                ),
                onMyStoryClick = {},
                onUserClick = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoriesListLoadingPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            StoriesList(
                myStoryUser = User("123", "Sajidha", hasUnviewedStory = false),
                users = emptyList(),
                onMyStoryClick = {},
                onUserClick = {},
                isLoading = true
            )
        }
    }
}
