package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import org.noormahal.vp25.android.presentation.viewmodel.FindScreenViewModel
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

@Composable
fun FindScreen(navController: NavController, viewModel: FindScreenViewModel) {
    val searchQuery by viewModel.searchString.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchString(it) },
                label = { Text("Search") },
                modifier = Modifier.weight(1f), // TextField takes available space
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search // Show search icon on keyboard
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.submitSearch()
                        keyboardController?.hide() // Hide keyboard on search action
                    }
                )
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between TextField and Button
            IconButton(
                onClick = {
                    viewModel.submitSearch()
                    keyboardController?.hide() // Hide keyboard on button click
                },
                enabled = !isLoading && searchQuery.isNotBlank() // Enable button if not loading and query is not blank
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }
//        TextField(
//            value = searchQuery,
//            onValueChange = { viewModel.setSearchString(it) },
//            label = { Text("Search") },
//            modifier = Modifier.fillMaxWidth()
//        )

        if (isLoading) {
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
            UserList(users = searchResults, onUserClick = { userId -> navController.navigate("profile/$userId") })
        }
    }
}

@Composable
fun UserList(users: List<PersonalizedProfile>, onUserClick: (String) -> Unit) {
    if (users.isEmpty()) {
        Text("No users found.", modifier = Modifier.padding(top = 16.dp))
        return
    }
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
        items(users) { user ->
            UserRow(user, onClick = { onUserClick(user.id) })
        }
    }
}

@Composable
fun UserRow(user: PersonalizedProfile, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = user.fullName)
    }
}