package org.noormahal.vp25.android.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "stories-table",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = arrayOf("userName"),
            childColumns = arrayOf("userName"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["remoteId"], unique = true)]
)
@Parcelize
data class Stories(
    @PrimaryKey(autoGenerate = true)
    val storyId:Long=0,
    @ColumnInfo
    val remoteId: String,
    @ColumnInfo
    val userName: String,
    @ColumnInfo
    val storyDetails: String,
    @ColumnInfo
    val durationSeconds: Long = 5,
    @ColumnInfo
    val viewed: Boolean = false,
    @ColumnInfo
    val timePosted: Long = System.currentTimeMillis()
):Parcelable

//for stories list page
data class User(
    val userName: String,
    val displayName: String,
    val hasUnviewedStory: Boolean = true
)

data class Story(
    val userName: String,
    val displayName: String,
    val storyId: Long,
    val remoteId: String,
    val storyDetails: String,
    val durationSeconds: Long,
    val timePosted: Long,
    val viewed: Boolean
)


