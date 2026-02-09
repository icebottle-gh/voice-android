package com.example.temp.presentation.navigation

import androidx.annotation.DrawableRes
import com.example.temp.R

//Sealed classes restrict inheritance to only the classes declared inside them.
//Objects are singletons, meaning only one instance of each exists in memory.
//Because BottomScreen and DrawerScreen extend Screen, each object inside them is also a Screen


sealed class Screen(val title: String, val route: String){

    object Login: Screen("Login","login_route")

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

//        object Chats: BottomScreen(
//            "Chats",
//            "chats_route",
//            R.drawable.baseline_chat_bubble_outline_24,
//            R.drawable.baseline_chat_bubble_24,
//            true,
//            56
//        )

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


        companion object {
            val fromRoute = fun(route: String): BottomScreen {
                return when (route) {
                    Stories.bottomRoute -> Stories
                    Notifications.bottomRoute -> Notifications
                    Find.bottomRoute -> Find
                    else -> throw IllegalArgumentException("Route $route does not match a BottomRoute.")
                }
            }
        }
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

    object StoriesDetail : Screen("User", "stories_detail/{userId}"){
        fun createRoute(userId: String) = "stories_detail/$userId"
    }

//    object StoriesDetailPage : Screen("Story", "stories_detail/{storyId}"){
//        fun createRoute(storyId: Long) = "stories_detail/$storyId"
//    }

}

val screensWithBottom = listOf(
//    Screen.BottomScreen.Chats,
    Screen.BottomScreen.Notifications,
    Screen.BottomScreen.Stories,
    Screen.BottomScreen.Find
)

val allScreens = listOf(

    Screen.Login,
    Screen.BottomScreen.Stories,
//    Screen.BottomScreen.Chats,
    Screen.BottomScreen.Notifications,
    Screen.BottomScreen.Find,

    Screen.StoriesDetail
)