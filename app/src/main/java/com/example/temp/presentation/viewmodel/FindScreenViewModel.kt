package com.example.temp.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.temp.common.Client
import com.example.temp.components.PersonListItemProps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.dto.PersonalizedProfile
import java.util.stream.Collectors

@OptIn(kotlinx.coroutines.FlowPreview::class)
class FindScreenViewModel(application: Application): AndroidViewModel(application) {
    private val _typing = MutableStateFlow(false)
    private val _loading = MutableStateFlow(false)
    private val _searchString = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<PersonListItemProps>>(emptyList())
    private val _searchInNetworkOnly = MutableStateFlow(false)
    val typing: StateFlow<Boolean> = _typing
    val loading: StateFlow<Boolean> = _loading
    val searchString: StateFlow<String> = _searchString
    val searchResults: StateFlow<List<PersonListItemProps>> = _searchResults
    val searchInNetworkOnly: StateFlow<Boolean> = _searchInNetworkOnly

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
                    val result = if (typing.value) {
                        Client.user!!.people().searchSuggest(query, _searchInNetworkOnly.value)
                    } else {
                        Client.user!!.people().search(query, _searchInNetworkOnly.value, 0)
                    }
                    _searchResults.value = result
                        .stream().map { makePersonListItemProps(it) }.collect(Collectors.toList())
                    _loading.value = false
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _typing.collectLatest { typing ->
                if (!typing) {
                    _loading.value = true
                    val result = Client.user!!.people().search(_searchString.value, _searchInNetworkOnly.value, 0)
                    _searchResults.value = result
                        .stream().map { makePersonListItemProps(it) }.collect(Collectors.toList())
                    _loading.value = false
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _searchInNetworkOnly.collectLatest { isNetworkOnly ->
                _loading.value = true
                val result = if (_typing.value) {
                    Client.user!!.people().searchSuggest(_searchString.value, isNetworkOnly)
                } else {
                    Client.user!!.people().search(_searchString.value, isNetworkOnly, 0)
                }
                _searchResults.value = result.stream().map { makePersonListItemProps(it) }
                    .collect(Collectors.toList())
                _loading.value = false
            }
        }
    }

    fun submitSearch() {
        _typing.value = false
    }

    fun setSearchString(query: String) {
        _searchString.value = query
    }

    fun setSearchInNetworkOnly(value: Boolean) {
        _searchInNetworkOnly.value = value
    }

    fun makePersonListItemProps(profile: PersonalizedProfile): PersonListItemProps {
        return PersonListItemProps(
            id = profile.id,
            fullName = profile.fullName,
            nickName = profile.nickName,
            isFollowing = true,
            isFollower = true
        )
    }
}