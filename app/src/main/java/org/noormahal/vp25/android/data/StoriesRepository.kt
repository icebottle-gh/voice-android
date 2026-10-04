package org.noormahal.vp25.android.data

import kotlinx.coroutines.flow.Flow

//Repository acts as the single source of truth between the ViewModel and the Database.
// Repository abstracts data sources, making it easy to switch between a database, API, or cache.(Scalability)
class StoriesRepository(
    private val storiesDao: StoriesDao,
    private val usersDao: UsersDao
) {
    fun getStoryListUsers(): Flow<List<User>> = storiesDao.getStoryListUsers()
    fun getStoriesOfUser(user:String): List<Story> = storiesDao.getStoriesofUser(user)

    fun markStoryAsViewed( storyId: Long){
        storiesDao.markStoryAsViewed( storyId = storyId)
    }

    suspend fun upsertUsers(users: List<Users>) = usersDao.upsertAll(users)
    suspend fun insertStories(stories: List<Stories>) = storiesDao.insertStories(stories)

}

fun org.noormahal.ib.vakkic.dto.Story.toEntity(): Stories = Stories(
    remoteId = id,
    userName = user,
    storyDetails = text,
    timePosted = createAt.time,
)

// The backend only ever gives us a displayName for the signed-in account
// itself (AccountInformation) - PersonalizedProfile, used for everyone else,
// has no such field. Leaving it null lets the COALESCE(displayName, fullName)
// fallback in StoriesDao's queries fill in a sensible name.
fun org.noormahal.ib.vakkic.dto.PersonalizedProfile.toUserEntity(): Users = Users(
    userName = id,
    fullName = fullName,
    nickName = nickName,
    displayName = null,
    bio = bio,
)