package com.example.temp.presentation.navigation

import androidx.annotation.DrawableRes
import com.example.temp.R

//Sealed classes restrict inheritance to only the classes declared inside them.
//Objects are singletons, meaning only one instance of each exists in memory.
//Because BottomScreen and DrawerScreen extend Screen, each object inside them is also a Screen


sealed class Screen(val title: String, val route: String){

    sealed class BottomScreen(
        val bottomTitle: String,
        val bottomRoute: String,
        @DrawableRes val unselectedIcon: Int,
        @DrawableRes val selectedIcon: Int,
        val hasNews: Boolean,
        val badgeCount: Int? = null
    ): Screen(bottomTitle,bottomRoute){
        object Stories: BottomScreen("Stories",
            "stories_route",
            R.drawable.baseline_dynamic_feed_24,
            R.drawable.baseline_dynamic_feed_24,
            true
        )

        object Chats: BottomScreen(
            "Chats",
            "chats_route",
            R.drawable.baseline_chat_bubble_outline_24,
            R.drawable.baseline_chat_bubble_24,
            true,
            56
        )

        object Notifications: BottomScreen(
            "Notifications",
            "notifications_route",
            R.drawable.baseline_notifications_none_24,
            R.drawable.baseline_notifications_24,
            true,
            55
        )

        object Find : BottomScreen(
            "Find",
            "find_people_route",
            R.drawable.baseline_person_search_24,
            R.drawable.baseline_person_search_24,
            false
        )

    }

//    sealed class DrawerScreen(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int)
//        : Screen(dTitle,dRoute){
//        object Account: DrawerScreen(
//        )
//        object Subscription: DrawerScreen(
//        )
//
//        object AddAccount: DrawerScreen()
//    }

    object StoriesDetail : Screen("User", "stories_detail/{postId}"){
        fun createRoute(postId: Long) = "stories_detail/$postId"
    }

}

val screensInBottom = listOf(
    Screen.BottomScreen.Stories,
    Screen.BottomScreen.Chats,
    Screen.BottomScreen.Notifications,
    Screen.BottomScreen.Find
)

val allScreens = listOf(

    Screen.BottomScreen.Stories,
    Screen.BottomScreen.Chats,
    Screen.BottomScreen.Notifications,
    Screen.BottomScreen.Find,

    Screen.StoriesDetail
)