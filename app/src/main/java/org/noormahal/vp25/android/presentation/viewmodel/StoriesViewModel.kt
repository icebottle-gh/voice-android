package org.noormahal.vp25.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.noormahal.vp25.android.data.Graph
//import org.noormahal.vp25.android.data.Stories
import org.noormahal.vp25.android.data.StoriesRepository
import org.noormahal.vp25.android.data.Story
import org.noormahal.vp25.android.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
//            getStoryListUsers = storiesRepository.getStoryListUsers()
            storiesRepository.getStoryListUsers().collect{
                _usersList.value = it
            }
        }

//        viewModelScope.launch {
//            _usersList.collectLatest {
//                userList->
//                for (user in userList){
//                    var userId = user.userId
//                    storiesRepository.getStoriesOfUser(user.userId).collect{
//                        stories->
//                        _userStoriesMap.value[userId] = stories
//                    }
//                }
//            }
//        }

//        viewModelScope.launch {
//
//            //get all stories of current user
//            _currentUser.collectLatest { userId ->
//                storiesRepository.getStoriesOfUser(userId).collect{ stories ->
//                    _currentUserStories.value =  stories
//                    _currentStoryIndex.value = getCurrentStoryIndex()
//
////                    _currentUserIndex.value = _getStoryListUsers.value.indexOfFirst {
////                        it.userId == _currentUser.value
////                    }
//                }
//            }
//
//        }
//
//        viewModelScope.launch{
//            //get current story to view for a user
//            //initial story - storystate of a user
//            _currentUserStories.collectLatest {
//                    stories->
//                val story = if (stories.isNotEmpty()) stories[getCurrentStoryIndex()] else Stories(userName = "", storyDetails = "NIL")
//                _initialStoryState.value = StoryState(loading = false, story = story)
//            }
//        }

//        //        Adding sample data
//        viewModelScope.launch {
//            for (story in dummyStories){
//                Thread.sleep(1_000)
//                val storyWithNewTime = story.copy(timePosted = System.currentTimeMillis())
//                storiesRepository.addNewStory(storyWithNewTime)
//            }
//
//        }


    }

//    fun getStoriesOfUser(userId: Long) {
//        viewModelScope.launch {
//            storiesRepository.getStoriesOfUser(userId).collect{ stories ->
//                _currentUserStories.value =  stories
//                _currentStoryIndex.value = getCurrentStoryIndex()
//            }
//        }
//    }


//    fun mapUserStories(){
//        viewModelScope.launch {
//            _usersList.value.forEach{
//                user->
//                launch {
//                    var userId = user.userId
//
////                    _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
////                        put(userId, emptyList())
////                    }
//                    storiesRepository.getStoriesOfUser(userId).collect{
//                        stories->
//                        _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
//                            put(userId, stories)
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun startStorySession() {
        _storySessionUsers.value = usersList.value.map { it.copy() }
//        // for now, maybe change FLOW to List in DAO
//        _storySessionStoriesMap.value = _userStoriesMap.value
//        println(storySessionUsers.value)
        mapUserStories()
    }

    //TODO - for when you change the return type in DAO of getting stories of each user, to list instead of flow. If there's no need for live data at all.
    fun mapUserStories() {
        viewModelScope.launch(Dispatchers.IO) {
            //note its storiessession value here
            _storySessionUsers.value.forEach{
                user->
//                launch {//each user diff coroutine
                    //apply mutex later maybe if you have to write

                    try {
                        val stories = storiesRepository.getStoriesOfUser(user.userName)
//                        delay(5000)
                        _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
                            put(user.userName, StoriesListState(loading = false, storiesList = stories))
                        }
                    }
                    catch (e: Exception){
                        _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
                            put(user.userName, StoriesListState(loading = false, error = "${e.message}"))
                        }
                    }

//                _userStoriesMap.value = _userStoriesMap.value.toMutableMap().apply {
//                    put(user.userId, storiesRepository.getStoriesOfUser(user.userId))
//                }


            }
        }
    }


    fun markStoryAsViewed(storyId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            storiesRepository.markStoryAsViewed(storyId=storyId)
        }
    }


//    fun getStoryState(story:Stories):StoryState{
//        return StoryState(loading = false, story = story)
//    }

//    data class StoryState(
//        val loading: Boolean = true,
//        val story: Stories = Stories(userName = "", storyDetails = ""),
//    )

    data class UserListState(
        val loading: Boolean = true,
        val userList: List<User>,
        val error: String?= null
    )

    data class StoriesListState(
        val loading: Boolean = true,
        val storiesList: List<Story> = emptyList(),
        val error: String? = null
    )
}