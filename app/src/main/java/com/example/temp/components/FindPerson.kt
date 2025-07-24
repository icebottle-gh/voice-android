package com.example.temp.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.temp.presentation.ui.UserList

@Composable
fun FindPerson(
    props: FindPersonProps,
    onSubmit: () -> Unit,
    onSearchStringChange: (String) -> Unit,
    onToggleConnection: (String) -> Unit,
    onFollow: (String) -> Unit,
    onUnfollow: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
                enabled = !props.isLoading && props.searchString.isNotBlank() // Enable button if not loading and query is not blank
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }

        if (props.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // .weight(1f) // Optional: if you want it to take remaining space when list is empty
                    .padding(vertical = 16.dp), // Add padding around the indicator
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp) // Example size
                )
            }
        } else {
            LazyColumn {
                items(props.searchResult.size) { index ->
                    val personListItemProps = props.searchResult[index]
                    val onFollowClick = {
                        if (personListItemProps.isFollowing) {
                            onUnfollow(personListItemProps.id)
                        } else {
                            onFollow(personListItemProps.id)
                        }
                    }
                    PersonListItem(props = personListItemProps, onFollowClick = onFollowClick)
                }
            }
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