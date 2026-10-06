package org.noormahal.vp25.android.data

import kotlinx.coroutines.flow.Flow

//Repository acts as the single source of truth between the ViewModel and the Database.
// Repository abstracts data sources, making it easy to switch between a database, API, or cache.(Scalability)
class StoriesRepository(
    private val storiesDao: StoriesDao,
    private val usersDao: UsersDao
) {
    private fun expiryCutoffMillis() = System.currentTimeMillis() - STORY_LIFETIME_MILLIS

    fun getStoryListUsers(): Flow<List<User>> = storiesDao.getStoryListUsers(expiryCutoffMillis())
    fun getStoriesOfUser(user:String): List<Story> = storiesDao.getStoriesofUser(user, expiryCutoffMillis())

    fun markStoryAsViewed( storyId: Long){
        storiesDao.markStoryAsViewed( storyId = storyId)
    }

    suspend fun upsertUsers(users: List<Users>) = usersDao.upsertAll(users)

    suspend fun insertStories(stories: List<Stories>) {
        // Opportunistic cleanup: piggyback on the one place new stories already
        // arrive, rather than standing up a separate periodic job for this.
        storiesDao.deleteStoriesOlderThan(expiryCutoffMillis())
        storiesDao.insertStories(stories)
    }

    suspend fun clearSeededDummyStories() = storiesDao.clearSeededDummyStories()

}

private const val STORY_LIFETIME_MILLIS = 24 * 60 * 60 * 1000L

// TODO: durationSeconds is a placeholder - the backend doesn't send it yet.
// Switch to the real value (likely a "durationSeconds" key alongside the
// story) once that lands; flag this in the PR if it hasn't by then.
fun org.noormahal.ib.vakkic.dto.Story.toEntity(): Stories = Stories(
    remoteId = id,
    userName = user,
    storyDetails = text,
    durationSeconds = 5,
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