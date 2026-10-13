package org.noormahal.vp25.android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class DraftDao {

    @Query("SELECT * FROM `draft-table` WHERE id = ${Draft.SLOT_ID}")
    abstract suspend fun getDraft(): Draft?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveDraft(draft: Draft)

    @Query("DELETE FROM `draft-table`")
    abstract suspend fun clearDraft()
}
