package com.example.temp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.temp.MainViewModel
import com.example.temp.R
import com.example.temp.StoriesViewModel
import com.example.temp.data.StoryList
import com.example.temp.presentation.navigation.Screen

@Composable
fun StoriesList(navController: NavHostController, mianViewModel: MainViewModel, storiesViewModel : StoriesViewModel) {
    val usersList = storiesViewModel.usersList.collectAsState(initial = listOf())
//    val usersList = storiesViewModel.getStoryListUsers
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding()){
        items(usersList.value){
            user->
//            storiesViewModel.getStoriesOfUser(user.userId)
//            var currentStory = storiesViewModel.getCurrentStory()
//            if (currentStory == null) {
//                Box {}
//            } else
//            {
                StoryItem(user = user) {
                    navController.navigate(Screen.StoriesDetail.createRoute(user.userId))
                }
//            }
        }
    }

}


@Composable
fun StoryItem(user: StoryList, onClick:()->Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                onClick()
            }
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(24.dp),
                color = if (!user.hasUnviewedStory)
                    colorResource(id = R.color.orange_A200)
                else
                    colorResource(id = R.color.grey_A400)

            ),

//        colors = CardDefaults.cardColors(containerColor = Color.White)
//        elevation = ,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Display user Profile Image
                if (user.userImageThumb != null) {
                    // Load profile image from URL or local resource
                    Image(
                        painter = rememberAsyncImagePainter(user.userImageThumb), // Use Coil/Glide if loading from URL
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                } else {
                    // Use default profile icon
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Default Profile Picture",
                        //TODO:color explicit here, change to be theme friendly
//                        tint = colorResource(id = R.color.grey_300),
                        modifier = Modifier
                            .size(40.dp)
                            .fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                //username
                Text(text = user.userName, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                //TODO:color explicit here, change to be theme friendly
                Text(text = "Just Now", fontSize = 12.sp) //color = colorResource(id = R.color.grey_A700))
            }

        }
    }
}
