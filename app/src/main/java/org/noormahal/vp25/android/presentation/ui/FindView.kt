package org.noormahal.vp25.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.noormahal.vp25.android.components.FIND_PERSON_TABS
import org.noormahal.vp25.android.components.FindPerson
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.viewmodel.FindScreenViewModel

private const val EVERYONE_TAB_INDEX = 0
private const val NETWORK_TAB_INDEX = 1

@Composable
fun FindView(navController: NavController, viewModel: FindScreenViewModel) {
    val searchQuery by viewModel.searchString.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val network by viewModel.network.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val loadingMore by viewModel.loadingMore.collectAsState()

    FindPerson(
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.setSearchString(it) },
        tabs = FIND_PERSON_TABS,
        selectedTabIndex = if (network) NETWORK_TAB_INDEX else EVERYONE_TAB_INDEX,
        onTabSelected = { viewModel.setNetwork(it == NETWORK_TAB_INDEX) },
        isLoading = isLoading,
        error = error,
        users = searchResults,
        onUserClick = { userId -> navController.navigate(Screen.PersonProfile.createRoute(userId)) },
        hasMore = hasMore,
        loadingMore = loadingMore,
        onLoadMore = { viewModel.loadNextPage() },
    )
}
