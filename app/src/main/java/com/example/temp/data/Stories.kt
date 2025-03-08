package com.example.temp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "stories-table")
@Parcelize
data class Stories(
    @PrimaryKey(autoGenerate = true)
    val storyId:Long=0,
    @ColumnInfo
    val userId: Long = 0L,
    @ColumnInfo
    val userImageThumb: String? = null,
    @ColumnInfo
    val userName: String,
    @ColumnInfo
    val storyDetails: String,
    @ColumnInfo
    val viewed: Boolean = false,
    @ColumnInfo
    val timePosted: Long = System.currentTimeMillis()
):Parcelable

//for stories list page
data class StoryList(
    val userId: Long,
    val userName: String,
    val userImageThumb: String? = null,
    val hasUnviewedStory: Boolean = true
)

//@Entity(tableName = "users-stories-table")
//@Parcelize
//data class StoriesUsers(
//    @PrimaryKey(autoGenerate = true)
//    val userId:Long = 0L,
//    val userName: String,
//    val postIdList: List<Stories>,
//    val newStories: Boolean = true
//):Parcelable

//data class StoriesResponse(
//    val stories: DummyStories
////    val stories: List<Posts>
//)
//
//object DummyStories{
//    val storieslist = listOf(
//        Stories(userId = 1, userName = "user1", storyDetails = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."),
//        Stories(userId = 1,userName = "user1", storyDetails = "hey this is just a two line text. to check the alignments."),
//        Stories(userId = 2,userName = "user2", storyDetails = "one line text here"),
//        Stories(userId = 3,userName = "user3", storyDetails = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\\n\" +\n" +
//                "            \"\\n\" +\n" +
//                "            \"Why do we use it?\\n\" +\n" +
//                "            \"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\\n\" +\n" +
//                "            \"\\n\" +\n" +
//                "            \"Why do we use it?\\n\" +\n" +
//                "            \"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)."),
//        Stories(userId = 5,userName = "user5", storyDetails = "Hey there just woke up. Hey there just woke up. Hey there just woke up. Hey there just woke up."),
//        Stories(userId = 5,userName = "user5", storyDetails = "Hey there just woke up. Hey there just woke up. Hey there just woke up. Hey there just woke up.") ,
//        Stories(userId = 5,userName = "user5", storyDetails = "Hey there just woke up. Hey there just woke up. Hey there just woke up. Hey there just woke up."),
//        Stories(userId = 5,userName = "user5", storyDetails = "Hey there just woke up. Hey there just woke up. Hey there just woke up. Hey there just woke up."),
//        Stories(userId = 5,userName = "user5", storyDetails = "Hey there just woke up. Hey there just woke up. Hey there just woke up. Hey there just woke up.")
//
//    )
//}
