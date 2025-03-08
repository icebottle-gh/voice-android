package com.example.temp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
abstract class StoriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addNewStory(story: Stories)

//    //get all stories
//    @Query("SELECT * FROM `STORIES-TABLE`")
//    abstract fun getAllStories():Flow<List<Stories>>

    //USERS LIST FOR STORY LIST PAGE
    // also orderd by viewed or not - if user has even one unviewed
    // ordered by timeposted asc of their last story
    @Query("""
                SELECT userId, userName, userImageThumb,
                    MAX(timePosted) AS latestStoryTime,
                    MIN(viewed) AS hasUnviewedStory
                FROM `STORIES-TABLE`
                GROUP BY userId, userName, userImageThumb
                ORDER BY hasUnviewedStory ASC, latestStoryTime DESC
    """)
    abstract fun getStoryListUsers(): Flow<List<StoryList>>

    //STORY LIST OF A SINGLE USER
    @Query("""
        SELECT userId,userName,userImageThumb, storyId,storyDetails, timePosted, viewed
        FROM `stories-table`
        WHERE userId==:userId
        ORDER BY timePosted ASC
    """)
    abstract fun getStoriesofUser(userId: Long): Flow<List<Stories>>

    //FIRST UNVIEWED STORY OF A USER



    //Update New story users (viewed = false) which users have new stories

    //delete the stories posted 24h ago

}