package com.example.temp.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.temp.R
import com.example.temp.StoriesViewModel
import com.example.temp.data.Stories

//import com.google.accompanist.placeholder.material3.placeholder

@Composable
fun StoriesDetail(userId: Long, storiesViewModel: StoriesViewModel, navController: NavHostController) {

    storiesViewModel.startStorySession(userId)
    storiesViewModel.mapUserStories()

    //users list userid, userName,..
    val storySessionUserList by storiesViewModel.storySessionUsers.collectAsState()
    //map userid -> list of stories
    val usersStoriesMap by storiesViewModel.userStoriesMap.collectAsState()

    val outerPagerState = rememberPagerState(
        initialPage = storySessionUserList.indexOfFirst { it.userId == userId }.coerceAtLeast(0),
        pageCount = { storySessionUserList.size }
    )
    
    HorizontalPager(
        state = outerPagerState,
        beyondViewportPageCount = 1
    ){
        userIndex ->
        var user = storySessionUserList[userIndex]
        //storylist
        var storiesList = usersStoriesMap[user.userId]?: emptyList()

        StoryPager(
            storiesViewModel,
            navController,
            storiesList
        )

    }

}

@Composable
fun StoryPager(
    storiesViewModel: StoriesViewModel,
    navController: NavHostController,
    stories: List<Stories>
){

    val storyPagerState = rememberPagerState(
        initialPage = stories.indexOfFirst {
            story->
            !story.viewed
        }.coerceAtLeast(0),
        pageCount = { stories.size }
    )

    HorizontalPager(
        state = storyPagerState,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 3
    ) {
        storyIndex->
        val currentStory = storiesViewModel.getStoryState(stories[storyIndex])
        DetailScaffold(currentStory,storiesViewModel,navController, storyPagerState.pageCount, storyIndex)
    }


}


@Composable
fun DetailScaffold(
    currentStory: StoriesViewModel.StoryState,
    storiesViewModel: StoriesViewModel,
    navController: NavHostController,
    storyCount: Int,
    storyIndex: Int
){
    Scaffold(
        topBar = { StoriesTopBar(currentStory.story.userName, storyCount, storyIndex) },
        bottomBar = { StoryDetailBottomBar() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                //                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Divider(thickness = 2.dp, color = colorResource(id = R.color.orange_A200))
            when{
                currentStory.loading -> {
                    CircularProgressIndicator(progress = 0.89f, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                else ->{
                    Text(
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp),
                        text = currentStory.story.storyDetails,
                        style = TextStyle(fontSize = 18.sp),
                        textAlign = TextAlign.Left,
                        lineHeight = 28.sp
                    )
                }
            }
            Divider(thickness = 2.dp, color = colorResource(id = R.color.orange_A200))


        }


    }

}

@Composable
fun StoriesTopBar(name: String, storyCount: Int, storyIndex: Int){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
    ){
        StoriesProgressIndicator(storyCount, storyIndex)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                        /*TODO the back button shouldn't go to previous story but to the storieslist screen or wherever they opened the stories detail from*/
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                text = name,
                fontSize = 18.sp,
                modifier = Modifier.heightIn(max = 30.dp)
            )
        }
    }
}
@Composable
fun StoriesProgressIndicator(storyCount: Int, storyIndex: Int) {
//    val totalStories = 5
//    val currentStoryIndex = 3
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        repeat(storyCount){
            index->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
//                        Color.Black
                        if (index <= storyIndex)
                            Color.Black
                        else
                            Color.Gray.copy(alpha = 0.5f)
                    )
            )
        }
    }
}


@Composable
fun StoryDetailBottomBar(){

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(56.dp),
        containerColor = Color.Transparent
    ){
        TextField(
            modifier = Modifier
                .weight(0.8f)
                .height(48.dp)
                .clip(RoundedCornerShape(28.dp)),
            value = "Reply",
            onValueChange = {
                //TODO
            },
            textStyle = TextStyle(fontSize = 14.sp),
            colors = TextFieldDefaults.colors(
                // making underline invisible
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(0.13f) // Increases space for the emoji
                .padding(start = 4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_insert_emoticon_24),
                contentDescription ="React",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

    }

}
