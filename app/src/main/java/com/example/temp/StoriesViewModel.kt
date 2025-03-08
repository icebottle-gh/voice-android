package com.example.temp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.temp.data.Graph
import com.example.temp.data.Stories
import com.example.temp.data.StoriesRepository
import com.example.temp.data.StoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoriesViewModel(
    private val storiesRepository: StoriesRepository = Graph.storiesRepository
): ViewModel() {

//    lateinit var getStoryListUsers = Flow<List<StoryList>>

    //that the variable will be initialised before it is called.
    private val _usersList: MutableStateFlow<List<StoryList>> = MutableStateFlow(emptyList())
    val usersList : StateFlow<List<StoryList>> = _usersList

    private val _storySessionUsers = MutableStateFlow<List<StoryList>>(emptyList())
    val storySessionUsers: StateFlow<List<StoryList>> = _storySessionUsers

    private val _currentUser : MutableStateFlow<Long> = MutableStateFlow(0)
    //    private val _currentUserIndex= MutableStateFlow(0)

    private val _currentUserStories: MutableStateFlow<List<Stories>> = MutableStateFlow(emptyList())
    val currentUserStories: StateFlow<List<Stories>> = _currentUserStories.asStateFlow()

    private val _currentStoryIndex = MutableStateFlow(0)

    private val _initialStoryState = MutableStateFlow(StoryState())
    val initialStoryState : StateFlow<StoryState> = _initialStoryState


//    private var unviewedUsers: List<StoryList> = listOf()
//    private var viewedUsers: List<StoryList> = listOf()
//    private var userList: List<StoryList> = listOf()

    //sample data
//    val dummyStories = DummyStories.storieslist
    init {
        viewModelScope.launch {
            //get the users list
//            getStoryListUsers = storiesRepository.getStoryListUsers()
            storiesRepository.getStoryListUsers().collect{
                _usersList.value = it
            }
        }

        viewModelScope.launch {

            //get all stories of current user
            _currentUser.collectLatest { userId ->
                storiesRepository.getStoriesOfUser(userId).collect{ stories ->
                    _currentUserStories.value =  stories
                    _currentStoryIndex.value = getCurrentStoryIndex()

//                    _currentUserIndex.value = _getStoryListUsers.value.indexOfFirst {
//                        it.userId == _currentUser.value
//                    }
                }
            }

        }

        viewModelScope.launch{
            //get current story to view for a user
            //initial story - storystate of a user
            _currentUserStories.collectLatest {
                    stories->
                val story = if (stories.isNotEmpty()) stories[getCurrentStoryIndex()] else Stories(userName = "", storyDetails = "NIL")
                _initialStoryState.value = StoryState(loading = false, story = story)
            }
        }

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
//    fun setCurrentUser(userId: Long){
//        _currentUser.value = userId
//    }
    fun getCurrentStoryIndex(): Int {
        return _currentUserStories.value.indexOfFirst {story ->
            !story.viewed
        }.coerceAtLeast(0)
    }
    fun startStorySession(userId: Long) {
        _storySessionUsers.value = usersList.value
        _currentUser.value = userId
    }



//    fun updateCurrentStory(){

//        // this causes to display the previously opened story, before getting the clicked user's current story
//        val stories = _currentUserStories.value
//        if(stories.isNotEmpty()){
//            _currentStoryState.value = CurrentStoryState(
//                loading = false,
//                story = stories[getCurrentStoryIndex()]
//            )
//        }
//        viewModelScope.launch {
//            _currentUserStories.collectLatest {
//                stories->
//                val story = if (stories.isNotEmpty()) stories[getCurrentStoryIndex()] else Stories(userName = "", storyDetails = "")
//                _currentStoryState.value = CurrentStoryState(loading = false, story = story)
//            }
//
//        }
//    }

    //NAVIGATION -> NEXT AND PREVIOUS STORY
//    fun divideUserList(){
//        if (usersList.value.isNotEmpty()){
//            unviewedUsers = usersList.value.filter { it.hasUnviewedStory }
//            viewedUsers = usersList.value.filter { !it.hasUnviewedStory }
//        }
//
//    }

    // Divide story list users into two - any unviewed and all viewed - userlist
    // userIndex = index of the user in the list
    // stories = list of current users' stories

    //1. If current user has more stories left to be viewed, go to next story of current user
    //2. If current user has no more stories left to be viewed go to next user if there's any in the userlist
    //3. If no users left after current user in userlist, back to storiesList page.
//    NAVIGATE TO NEXT STORY OF USER
//    fun nextStory(navController: NavController){
//
//        var userList : List<StoryList> = emptyList()
//        var userIndex : Int =0
////        val stories = _currentUserStories.value
//        if (_usersList.value.isNotEmpty()){
//            userList = if (unviewedUsers.any { it.userId == _currentUser.value }) {
//                unviewedUsers
//            } else {
//                viewedUsers
//            }
//            userIndex = userList.indexOfFirst {
//                it.userId == _currentUser.value
//            }
//        }
//
//
//        //if current user has more stories left -> go to next story
//        if (_currentStoryIndex.value < _currentUserStories.value.size-1){
//            _currentStoryIndex.value++
//        }else if(userIndex < userList.size-1){
//            //current user has no more stories, and there is next user in userlist
//            _currentUser.value++
//        }else{
//            //there is no next user in userlist
//            navController.popBackStack()
//        }
//
//    }

    //NAVIGATE TO PREV STORY OF USER
    fun prevStory(navController: NavController){


    }

    data class StoryState(
        val loading: Boolean = true,
        val story: Stories = Stories(userName = "", storyDetails = ""),
    )

}