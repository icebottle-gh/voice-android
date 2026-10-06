package org.noormahal.vp25.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.common.Client
import org.noormahal.vp25.android.common.CURRENT_USER_USERNAME
import org.noormahal.vp25.android.data.Graph
import org.noormahal.vp25.android.data.StoriesRepository
import org.noormahal.vp25.android.data.Story
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.data.toEntity
import org.noormahal.vp25.android.data.toUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoriesViewModel(
    private val storiesRepository: StoriesRepository = Graph.storiesRepository
): ViewModel() {

    private val _usersList: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val usersList : StateFlow<List<User>> = _usersList


    private val _storySessionUsers = MutableStateFlow<List<User>>(emptyList())
    val storySessionUsers: StateFlow<List<User>> = _storySessionUsers


    //State
    private val _userStoriesMap = MutableStateFlow<MutableMap<String, StoriesListState>>(mutableMapOf())
    val userStoriesMap: StateFlow<MutableMap<String, StoriesListState>> = _userStoriesMap


    init {

        // user list flow
        viewModelScope.launch {
            storiesRepository.getStoryListUsers().collect{
                _usersList.value = it
            }
        }

        // TEMP: one-time cleanup of the local-only dummy rows inserted while
        // story-posting was broken on the dev backend (see
        // project_story_send_backend_broken / project_temporary_stories_local_seed
        // memories) - that's fixed now, so real stories replace them via
        // refreshStories() below. Remove this call once confirmed clean.
        viewModelScope.launch(Dispatchers.IO) { storiesRepository.clearSeededDummyStories() }

        viewModelScope.launch { refreshStories() }
    }

    suspend fun refreshStories() {
        withContext(Dispatchers.IO) {
            try {
                val remoteStories = Client.user!!.stories().pull()
                val usernames = remoteStories.map { it.user }.distinct()
                val profiles = if (usernames.isEmpty()) emptyList()
                    else Client.user!!.people().getProfiles(usernames)
                storiesRepository.upsertUsers(profiles.map { it.toUserEntity() })

                val knownUsernames = profiles.map { it.id }.toSet()
                storiesRepository.insertStories(
                    remoteStories.filter { it.user in knownUsernames }.map { it.toEntity() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Client.reportIfUnauthorized(e)
            }
        }
    }

    fun startStorySession() {
        // The underlying query sorts by hasUnviewedStory/recency, same as the list
        // screen - but the list screen pins "my story" first regardless of that (see
        // StoriesListView.kt), so the detail session needs the same pin to stay
        // consistent, instead of landing wherever those criteria put it. sortedBy is
        // stable, so everyone else keeps the query's own relative order.
        _storySessionUsers.value = usersList.value
            .sortedBy { if (it.userName == CURRENT_USER_USERNAME) 0 else 1 }
            .map { it.copy() }
        mapUserStories()
    }

    fun mapUserStories() {
        viewModelScope.launch(Dispatchers.IO) {
            //note its storiessession value here
            _storySessionUsers.value.forEach{
                user->
                    //apply mutex later maybe if you have to write
                    try {
                        val stories = storiesRepository.getStoriesOfUser(user.userName)
                        _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
                            put(user.userName, StoriesListState(loading = false, storiesList = stories))
                        }
                    }
                    catch (e: Exception){
                        _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
                            put(user.userName, StoriesListState(loading = false, error = "${e.message}"))
                        }
                    }
            }
        }
    }


    fun markStoryAsViewed(story: Story, reportToBackend: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            storiesRepository.markStoryAsViewed(storyId = story.storyId)
            if (reportToBackend) {
                try {
                    val receipt = Client.user!!.stories().makeOpenReceipt(story.remoteId)
                    Client.user!!.stories().sendReceipt(listOf(receipt))
                } catch (e: Exception) {
                    e.printStackTrace()
                    Client.reportIfUnauthorized(e)
                }
            }
        }
    }


    data class StoriesListState(
        val loading: Boolean = true,
        val storiesList: List<Story> = emptyList(),
        val error: String? = null
    )
}