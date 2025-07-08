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
    //userName, displayName, timePosted, viewed
    // also orderd by viewed or not - if user has even one unviewed
    // ordered by timeposted asc of their last story
    @Query("""
                SELECT STORIES.userName, displayName,
                    MAX(timePosted) AS latestStoryTime,
                    MIN(viewed)=0 AS hasUnviewedStory
                FROM `STORIES-TABLE` AS STORIES 
                JOIN `USERS-TABLE` AS USERS 
                ON STORIES.userName=USERS.userName 
                GROUP BY STORIES.userName,displayName
                ORDER BY hasUnviewedStory DESC, latestStoryTime DESC
    """)
    abstract fun getStoryListUsers(): Flow<List<User>>

    //STORY LIST OF A SINGLE USER
    //userName, displayName, storyId, storyDetails, timePosted, viewed
    @Query("""
        SELECT stories.userName, users.displayName, stories.storyId,stories.storyDetails, stories.timePosted, stories.viewed
        FROM `stories-table` as stories 
        JOIN `users-table` as users 
        ON stories.userName=users.userName
        WHERE stories.userName==:user
        ORDER BY timePosted ASC
    """)
    abstract fun getStoriesofUser(user: String): List<Story>

    //UPDATE VIEW STATUS
    @Query("UPDATE `stories-table` SET viewed = 1 WHERE storyId = :storyId")
    abstract fun markStoryAsViewed(storyId: Long)

    //delete the stories posted 24h ago

}