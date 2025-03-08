package com.example.temp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Stories::class],
    version = 1,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {
    //Register the DAO
    abstract fun StoriesDao(): StoriesDao
}