package com.example.temp.data

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: VakkiDatabase

    val storiesRepository by lazy {
        StoriesRepository(storiesDao = database.StoriesDao())
    }
    fun provide(context:Context){
        database = Room.databaseBuilder(
                context,
                VakkiDatabase::class.java,
                "vakki.db"
            ).build()
    }
}