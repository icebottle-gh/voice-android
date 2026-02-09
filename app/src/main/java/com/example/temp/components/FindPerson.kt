package com.example.temp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindPerson(
    searchString: String,
    isConnection: Boolean,
    searchResult: List<PersonListItemProps>,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onSearchStringChange: (String) -> Unit,
    onToggleConnection: (Boolean) -> Unit,
    onFollow: (String) -> Unit,
    onUnfollow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp),
        ) {
            OutlinedTextField(
                value = searchString,
                onValueChange = { onSearchStringChange(it) },
                label = { Text("Search") },
                modifier = Modifier.weight(1f), // TextField takes available space
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search // Show search icon on keyboard
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSubmit()
                        keyboardController?.hide() // Hide keyboard on search action
                    }
                )
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between TextField and Button
            IconButton(
                onClick = {
                    onSubmit()
                    keyboardController?.hide() // Hide keyboard on button click
                },
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = !isLoading // Enable button if not loading and query is not blank
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SecondaryTabRow(
                modifier = Modifier.padding(),
                selectedTabIndex = if (isConnection) 1 else 0
            ) {
                Tab(
                    selected = !isConnection,
                    onClick = { onToggleConnection(false) },
                    text = {
                        Text(
                            text = "Everyone",
                        )
                    }
                )

                Tab(
                    selected = isConnection,
                    onClick = { onToggleConnection(true) },
                    text = {
                        Text(
                            text = "Network",
                        )
                    }
                )
            }
        }

        PersonList(
            props = PersonListProps(list = searchResult, isLoading = isLoading),
            onFollow = onFollow,
            onUnfollow = onUnfollow
        )
    }
}

data class FindPersonProps(
    var searchString: String = "",
    var isConnection: Boolean = false,
    var searchResult: List<PersonListItemProps>,
    var isLoading: Boolean = false
)

@Preview(showBackground = true)
@Composable
fun FindPersonPreview(modifier: Modifier = Modifier) {
    val searchResult = listOf(
        PersonListItemProps(
            id = "1",
            fullName = "John Doe",
            nickName = "Johnny",
            isFollowing = true,
            isFollower = true
        ),
        PersonListItemProps(
            id = "2",
            fullName = "Jane Smith",
            nickName = null,
            isFollowing = false,
            isFollower = false
        )
    )
    FindPerson(searchString = "John", isConnection = true,
        searchResult = searchResult, isLoading = false, {}, {}, {}, {}, {}, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun FindPersonLoadingPreview() {
    FindPerson(searchString = "John", isConnection = false,
        searchResult = emptyList(), isLoading = true, {}, {}, {}, {}, {})
}