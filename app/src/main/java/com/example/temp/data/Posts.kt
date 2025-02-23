package com.example.temp.data

data class Posts(
    val postId:Long=0L,
    val userImageThumb: String? = null,
    val userName: String,
    val postDetails: String,
//    val postTime: String
)

object DummyPosts{
    val postslist = listOf(
        Posts(userName = "user1", postDetails = "Hey there just woke up. "),
        Posts(userName = "user1", postDetails = "Hey there just woke up. "),
        Posts(userName = "user2", postDetails = "Hey there just woke up. "),
        Posts(userName = "user3", postDetails = "Hey there just woke up. "),
//        Posts(userName = "user1", postDetails = "Hey there just woke up. "),
//        Posts(userName = "user5", postDetails = "Hey there just woke up. Hey there just woke up. Hey there just woke up. Hey there just woke up."),
//        Posts(userName = "user1", postDetails = "Hey there just woke up. "),
//        Posts(userName = "user2", postDetails = "Hey there just woke up. "),
//        Posts(userName = "user1", postDetails = "Hey there just woke up. "),
//        Posts(userName = "user3", postDetails = "Hey there just woke up. "),
//        Posts(userName = "user1", postDetails = "Hey there just woke up. ")
    )
}