package org.noormahal.vp25.android.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.common.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

private const val PAGE_SIZE = 25

@OptIn(FlowPreview::class)
class FindScreenViewModel(application: Application): AndroidViewModel(application) {
    private val _loading = MutableStateFlow(false)
    private val _loadingMore = MutableStateFlow(false)
    private val _searchString = MutableStateFlow("")
    // false = search everyone, true = search within your network/connections only -
    // this is what the "Everyone"/"Network" tabs actually switch.
    private val _network = MutableStateFlow(false)
    private val _searchResults = MutableStateFlow<List<PersonalizedProfile>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val _hasMore = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading
    val loadingMore: StateFlow<Boolean> = _loadingMore
    val searchString: StateFlow<String> = _searchString
    val network: StateFlow<Boolean> = _network
    val searchResults: StateFlow<List<PersonalizedProfile>> = _searchResults
    val error: StateFlow<String?> = _error
    val hasMore: StateFlow<Boolean> = _hasMore

    private var currentPage = 0


    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(searchString, network) { query, network -> query to network }
                .debounce(300)
                .collectLatest { (query, network) ->
                    currentPage = 0
                    _loading.value = true
                    try {
                        val results = Client.user!!.people().search(query, network, currentPage)
                        _searchResults.value = results
                        _hasMore.value = results.size >= PAGE_SIZE
                        _error.value = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Client.reportIfUnauthorized(e)
                        _error.value = e.message ?: "Something went wrong. Please try again."
                    } finally {
                        _loading.value = false
                    }
                }
        }
    }

    fun loadNextPage() {
        if (_loading.value || _loadingMore.value || !_hasMore.value) return
        val query = _searchString.value
        val network = _network.value
        val nextPage = currentPage + 1
        viewModelScope.launch(Dispatchers.IO) {
            _loadingMore.value = true
            try {
                val results = Client.user!!.people().search(query, network, nextPage)
                // Discard the page if the query/tab changed while this request was in flight.
                if (query == _searchString.value && network == _network.value) {
                    currentPage = nextPage
                    _searchResults.value = _searchResults.value + results
                    _hasMore.value = results.size >= PAGE_SIZE
                }
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
                _error.value = e.message ?: "Something went wrong. Please try again."
            } finally {
                _loadingMore.value = false
            }
        }
    }

    fun setSearchString(query: String) {
        _searchString.value = query
    }

    fun setNetwork(network: Boolean) {
        _network.value = network
    }
}
