package com.example.temp.presentation.ui

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
import com.example.temp.components.FindPerson
import com.example.temp.presentation.viewmodel.FindScreenViewModel
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

@Composable
fun FindScreen(navController: NavController, viewModel: FindScreenViewModel) {
    val searchQuery by viewModel.searchString.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val searchInNetworkOnly by viewModel.searchInNetworkOnly.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    FindPerson(
        searchString = searchQuery,
        isConnection = searchInNetworkOnly,
        searchResult = searchResults,
        isLoading = isLoading,
        onSubmit = {
            keyboardController?.hide()
            viewModel.submitSearch()
        },
        onSearchStringChange = viewModel::setSearchString,
        onToggleConnection = viewModel::setSearchInNetworkOnly,
        onFollow = {},
        onUnfollow = {}
    )
}