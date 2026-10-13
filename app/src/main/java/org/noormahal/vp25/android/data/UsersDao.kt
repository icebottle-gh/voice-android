package org.noormahal.vp25.android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

@Dao
abstract class UsersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertIgnoring(users: List<Users>): List<Long>

    @Update
    abstract suspend fun update(users: List<Users>)

    @Transaction
    open suspend fun upsertAll(users: List<Users>) {
        val insertResults = insertIgnoring(users)
        val toUpdate = users.filterIndexed { index, _ -> insertResults[index] == -1L }
        if (toUpdate.isNotEmpty()) {
            update(toUpdate)
        }
    }
}
