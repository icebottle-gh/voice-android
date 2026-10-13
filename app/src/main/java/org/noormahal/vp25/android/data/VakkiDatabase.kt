package org.noormahal.vp25.android.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Users::class, Stories::class, Draft::class],
    version = 7,
    exportSchema = false
)
abstract class VakkiDatabase : RoomDatabase() {
    //Register the DAO
    abstract fun StoriesDao(): StoriesDao
    abstract fun UsersDao(): UsersDao
    abstract fun DraftDao(): DraftDao
}
