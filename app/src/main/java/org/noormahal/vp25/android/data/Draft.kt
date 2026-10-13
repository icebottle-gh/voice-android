package org.noormahal.vp25.android.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Single-slot draft - id is always SLOT_ID so there's ever only one row, and
// saving just replaces it rather than accumulating a list of drafts.
//
// text is the markup-encoded story (same format sent to the backend, see
// common/StoryMarkup.kt) rather than plain text + separate format flags - formatting
// is per-character-range now, not a single whole-story toggle, and markup already
// carries that precisely. Resuming a draft parses it back into ranges for editing.
@Entity(tableName = "draft-table")
data class Draft(
    @PrimaryKey
    val id: Int = SLOT_ID,
    @ColumnInfo
    val text: String
) {
    companion object {
        const val SLOT_ID = 0
    }
}
