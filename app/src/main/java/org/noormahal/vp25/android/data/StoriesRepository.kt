package org.noormahal.vp25.android.data

import kotlinx.coroutines.flow.Flow

//Repository acts as the single source of truth between the ViewModel and the Database.
// Repository abstracts data sources, making it easy to switch between a database, API, or cache.(Scalability)
class StoriesRepository(private val storiesDao: StoriesDao) {
    suspend fun addNewStory(story: Stories){
        storiesDao.addNewStory(story)
    }
    fun getStoryListUsers(): Flow<List<User>> = storiesDao.getStoryListUsers()
    fun getStoriesOfUser(user:String): List<Story> = storiesDao.getStoriesofUser(user)

    fun markStoryAsViewed( storyId: Long){
        storiesDao.markStoryAsViewed( storyId = storyId)
    }

}