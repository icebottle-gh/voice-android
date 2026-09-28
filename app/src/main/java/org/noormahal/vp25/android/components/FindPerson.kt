package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.ib.vakkic.dto.PersonalizedProfile
import org.noormahal.vp25.android.common.makePersonalizedProfile
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

// How many items from the end of the list to start loading the next page.
private const val LOAD_MORE_THRESHOLD = 3

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
    val showLoadingSpinner = rememberDelayedLoading(isLoading)
    val listState = rememberLazyListState()

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

    Column(modifier = modifier.fillMaxSize()) {
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

        if (showLoadingSpinner && users.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        } else if (users.isEmpty()) {
            // If a search fails while there are already results on screen, those are left
            // as-is rather than replaced with an error - only shown here when there's
            // nothing else to display.
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
                items(users) { user ->
                    PersonListItem(
                        name = user.fullName,
//                        subtitle = "",
                        showAvatar = true,
                        onClick = { onUserClick(user.id) }
                    )
                }
                if (loadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
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
