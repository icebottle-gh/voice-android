package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.ib.vakkic.dto.PersonalizedProfile
import org.noormahal.vp25.android.common.makePersonalizedProfile
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme


private const val LOAD_MORE_THRESHOLD = 3

private const val INITIAL_SKELETON_ROWS = 8
private const val APPENDED_SKELETON_ROWS = 2

val FIND_PERSON_TABS = listOf("Everyone", "Network")

@Composable
fun FindPerson(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    isLoading: Boolean,
    error: String? = null,
    users: List<PersonalizedProfile>,
    onUserClick: (String) -> Unit,
    hasMore: Boolean = false,
    loadingMore: Boolean = false,
    onLoadMore: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(listState, users, hasMore, loadingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (hasMore && !loadingMore && lastVisibleIndex != null &&
                    lastVisibleIndex >= users.size - LOAD_MORE_THRESHOLD
                ) {
                    onLoadMore()
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
            .focusable(false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = VpSpacing.screenHorizontal, vertical = 8.dp)
        ) {
            VpSearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            VpTabRow(
                titles = tabs,
                selectedIndex = selectedTabIndex,
                onTabSelected = onTabSelected
            )
        }

        if (isLoading && users.isEmpty()) {
            Column {
                repeat(INITIAL_SKELETON_ROWS) {
                    PersonListItemSkeleton()
                }
            }
        } else if (users.isEmpty()) {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = VpSpacing.screenHorizontal, vertical = 16.dp)
                )
            } else {
                Text(
                    text = "No users found.",
                    modifier = Modifier.padding(horizontal = VpSpacing.screenHorizontal, vertical = 16.dp)
                )
            }
        } else {
            LazyColumn(state = listState) {
                if (isLoading) {
                    items(APPENDED_SKELETON_ROWS) {
                        PersonListItemSkeleton()
                    }
                }
                items(users) { user ->
                    PersonListItem(
                        name = user.fullName,
//                        subtitle = "",
                        showAvatar = true,
                        onClick = { onUserClick(user.id) }
                    )
                }
                if (loadingMore) {
                    items(APPENDED_SKELETON_ROWS) {
                        PersonListItemSkeleton()
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FindPersonEmptyPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FindPerson(
                searchQuery = "",
                onSearchQueryChange = {},
                tabs = FIND_PERSON_TABS,
                selectedTabIndex = 0,
                onTabSelected = {},
                isLoading = false,
                users = emptyList(),
                onUserClick = {},
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FindPersonResultsPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FindPerson(
                searchQuery = "",
                onSearchQueryChange = {},
                tabs = FIND_PERSON_TABS,
                selectedTabIndex = 0,
                onTabSelected = {},
                isLoading = false,
                users = listOf(
                    makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = null, bio = null),
                    makePersonalizedProfile(id = "2", fullName = "James", nickName = null, bio = null),
                    makePersonalizedProfile(id = "3", fullName = "Jane Smith", nickName = null, bio = null),
                ),
                onUserClick = {},
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FindPersonErrorPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FindPerson(
                searchQuery = "jo",
                onSearchQueryChange = {},
                tabs = FIND_PERSON_TABS,
                selectedTabIndex = 0,
                onTabSelected = {},
                isLoading = false,
                error = "Something went wrong. Please try again.",
                users = emptyList(),
                onUserClick = {},
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FindPersonLoadingPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FindPerson(
                searchQuery = "jo",
                onSearchQueryChange = {},
                tabs = FIND_PERSON_TABS,
                selectedTabIndex = 0,
                onTabSelected = {},
                isLoading = true,
                users = emptyList(),
                onUserClick = {},
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FindPersonRefiningPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FindPerson(
                searchQuery = "jo",
                onSearchQueryChange = {},
                tabs = FIND_PERSON_TABS,
                selectedTabIndex = 0,
                onTabSelected = {},
                isLoading = true,
                users = listOf(
                    makePersonalizedProfile(id = "1", fullName = "John Doe", nickName = null, bio = null),
                    makePersonalizedProfile(id = "2", fullName = "James", nickName = null, bio = null),
                ),
                onUserClick = {},
            )
        }
    }
}
