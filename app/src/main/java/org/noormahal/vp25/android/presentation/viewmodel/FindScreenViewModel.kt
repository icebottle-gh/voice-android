package org.noormahal.vp25.android.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import org.noormahal.vp25.android.common.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

@OptIn(FlowPreview::class)
class FindScreenViewModel(application: Application): AndroidViewModel(application) {
    private val _typing = MutableStateFlow(false)
    private val _loading = MutableStateFlow(false)
    private val _searchString = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<PersonalizedProfile>>(emptyList())
    val typing: StateFlow<Boolean> = _typing
    val loading: StateFlow<Boolean> = _loading
    val searchString: StateFlow<String> = _searchString
    val searchResults: StateFlow<List<PersonalizedProfile>> = _searchResults

    /**
     * Listen to searchString, and typing. Update loading flag and searchResult.
     * Use [org.noormahal.ib.vakkic.User.people.search] to search for users.
     */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            searchString
                .debounce(300)
                .onEach { query ->
                    _loading.value = true
                    if (_typing.value == false) {
                        _typing.value = true
                    }
                }
                .collectLatest { query ->

                    _searchResults.value = Client.user!!.people().search(query, _typing.value)
                    _loading.value = false
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _typing.collectLatest { typing ->
                if (!typing) {
                    _loading.value = true
                    _searchResults.value = Client.user!!.people().search(_searchString.value, _typing.value)
                    _loading.value = false
                }
            }
        }
    }

    fun submitSearch() {
        _typing.value = false
    }

    fun setSearchString(query: String) {
        _searchString.value = query
    }
}