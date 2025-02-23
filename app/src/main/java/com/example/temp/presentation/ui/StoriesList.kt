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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.temp.MainViewModel
import com.example.temp.data.DummyPosts
import com.example.temp.data.Posts
import com.example.temp.presentation.navigation.Screen

@Composable
fun StoriesList(navController: NavHostController, viewModel: MainViewModel) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp)){
        items(DummyPosts.postslist){
                post->
            PostItem(post = post) {
                navController.navigate(Screen.StoriesDetail.createRoute(post.postId))
            }
        }
    }
}


@Composable
fun PostItem(post: Posts, onClick:()->Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                onClick()
            }
            .border(
                if (post.userName == "user1") {
                    2.dp
                } else {
                    (-1).dp
                },

                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
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
                if (post.userImageThumb != null) {
                    // Load profile image from URL or local resource
                    Image(
                        painter = rememberAsyncImagePainter(post.userImageThumb), // Use Coil/Glide if loading from URL
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
                Text(text = post.userName, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                //TODO:color explicit here, change to be theme friendly
                Text(text = "Just Now", fontSize = 12.sp) //color = colorResource(id = R.color.grey_A700))
            }

        }
    }
}

