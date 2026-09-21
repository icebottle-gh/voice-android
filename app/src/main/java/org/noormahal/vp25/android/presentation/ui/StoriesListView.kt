package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.noormahal.vp25.android.components.StoryListItem
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun StoriesList(navController: NavHostController, storiesViewModel : StoriesViewModel) {
//    val usersList = storiesViewModel.usersList.collectAsState(initial = listOf())

    val usersList = MutableStateFlow(listOf(
        User("Saji", "Sajidha Abdulla", true),
        User("sali", "Muhammed Salih", true),
        User("hahi", "Ravi", true),
        User("kiki", "Ahmed", false),
        User("chuchu", "Dani", false),
    ))


    // also get your stories
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        //Your Story card here
        item {
            StoryListItem(
                user = User(
                    "123",
                    "Sajidha",
                    hasUnviewedStory = false
                ),
                isMyStory = true
            ) {
                //navigate to my stories detail
            }
        }

        //other users stories card
        items(usersList.value){
                user->
            StoryListItem(user = user) {
                navController.navigate(Screen.StoriesDetail.createRoute(user.userName))
            }
        }
    }

}
//    val usersList = storiesViewModel.getStoryListUsers