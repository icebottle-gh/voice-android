package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
    props: FindPersonProps,
    onSubmit: () -> Unit,
    onSearchStringChange: (String) -> Unit,
    onToggleConnection: (Boolean) -> Unit,
    onFollow: (String) -> Unit,
    onUnfollow: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold() { contentPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {
                OutlinedTextField(
                    value = props.searchString,
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
                    enabled = !props.isLoading // Enable button if not loading and query is not blank
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }

            SecondaryTabRow(
                modifier = Modifier.padding(contentPadding),
                selectedTabIndex = if (props.isConnection) 1 else 0
            ) {
                Tab(
                    selected = !props.isConnection,
                    onClick = { onToggleConnection(false) },
                    text = {
                        Text(
                            text = "Everyone",
                        )
                    }
                )

                Tab(
                    selected = props.isConnection,
                    onClick = { onToggleConnection(true) },
                    text = {
                        Text(
                            text = "Network",
                        )
                    }
                )
            }

            PersonList(
                props = PersonListProps(list = props.searchResult, isLoading = props.isLoading),
                onFollow = onFollow,
                onUnfollow = onUnfollow
            )
        }
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
fun FindPersonPreview() {
    val props = FindPersonProps(
        searchString = "John",
        isConnection = true,
        searchResult = listOf(
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
        ),
        isLoading = false
    )
    FindPerson(props, {}, {}, {}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun FindPersonLoadingPreview() {
    val props = FindPersonProps(
        searchString = "John",
        isConnection = false,
        searchResult = emptyList(),
        isLoading = true
    )
    FindPerson(props, {}, {}, {}, {}, {})
}