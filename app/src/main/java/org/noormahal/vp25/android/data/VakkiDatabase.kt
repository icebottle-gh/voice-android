package org.noormahal.vp25.android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Users::class, Stories::class, AppSecret::class],
    version = 2,
    exportSchema = false
)
abstract class VakkiDatabase : RoomDatabase() {
    //Register the DAO
    abstract fun StoriesDao(): StoriesDao
    abstract fun appSecretDao(): AppSecretDao

    companion object {
        @Volatile // Ensures visibility of this variable across threads
        private var INSTANCE: VakkiDatabase? = null

        fun getDatabase(context: Context): VakkiDatabase {
            return INSTANCE ?: synchronized(this) { // synchronized to prevent race conditions
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VakkiDatabase::class.java,
                    "vakki.db" // Name of your database file
                )
                    .fallbackToDestructiveMigration(false) // For prototyping, if you change schema and don't want to write migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}