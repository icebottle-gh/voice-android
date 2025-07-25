package com.example.temp.presentation.ui

//import com.example.temp.data.Stories
import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import com.example.temp.R
import com.example.temp.data.Story
import com.example.temp.data.User
import com.example.temp.presentation.viewmodel.StoriesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun StoriesDetail(userName: String, storiesViewModel: StoriesViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val view = LocalView.current

    LaunchedEffect(Unit) {
        storiesViewModel.startStorySession()
        val window = (context as? Activity)?.window
        val controller = window?.let {
            WindowInsetsControllerCompat(it, view)
        }
        controller?.hide(WindowInsetsCompat.Type.statusBars())
        controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    DisposableEffect(Unit) {
        onDispose {
            val window = (context as? Activity)?.window
            val controller = window?.let {
                WindowInsetsControllerCompat(it, view)
            }
            controller?.show(WindowInsetsCompat.Type.statusBars())
        }
    }

//    users list userid, userName,..
//    val storySessionUserList by storiesViewModel.storySessionUsers.collectAsState()
//    val storySessionStoriesMap by storiesViewModel.userStoriesMap.collectAsState()

    val storySessionUserList = listOf(
        User("Saji", "Sajidha Abdulla", true),
        User("sali", "Muhammed Salih", true),
        User("hahi", "Hahahahahha", true),
        User("kiki", "Kiki Kuku", false),
        User("chuchu","Chuchu",false)
    )

    val storySessionStoriesMap = mutableMapOf<String, StoriesViewModel.StoriesListState>(
        "saji" to StoriesViewModel.StoriesListState(
            false,
            listOf(
                Story("Saji", "Sajidha Abdulla", 1, "Hi", 1, true),
                Story("Saji", "Sajidha Abdulla", 2, "Lorem Ipsum is simply dummy text.", 2, false),
                Story(
                    "Saji",
                    "Sajidha Abdulla",
                    3,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry.Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    3,
                    false
                ),
                Story(
                    "Saji",
                    "Sajidha Abdulla",
                    4,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. ",
                    20,
                    false
                ),
            )
        ),
        "sali" to StoriesViewModel.StoriesListState(
            false,
            listOf(
                Story("sali","Muhammed Salih", 5, "There are many variations",2, false),
                Story("sali","Muhammed Salih", 6, "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33",18, false)

            )
        ),
        "hahi" to StoriesViewModel.StoriesListState(
            false,
            listOf(
                Story("hahi","Haha", 7, "A",7,true),
                Story("hahi","Haha", 8, "B",8,true),
                Story("hahi","Haha", 9, "C",9,false),
                Story("hahi","Haha", 10, "D",10,false),
                Story("hahi","Haha", 11, "E",11,false),
            )
        ),
        "kiki" to StoriesViewModel.StoriesListState(
            false,
            listOf(
                Story("kiki","Kiki",12, "Hey seen",8, true)
            )
        ),
        "chuchu" to StoriesViewModel.StoriesListState(
            false,
            listOf(
                Story("chuchu","Chuchu",13,"Hey seen too",9,false)
            )
        ),
    )


    val userPagerState = rememberPagerState(
        //0 when opening first few times after opening the app. Kinda Race condition
        initialPage = storySessionUserList.indexOfFirst { it.userName == userName }.coerceAtLeast(0),
        pageCount = { storySessionUserList.size }
    )
    //temporary fix for above race.
    LaunchedEffect(storySessionUserList){
        userPagerState.scrollToPage(storySessionUserList.indexOfFirst { it.userName == userName }.coerceAtLeast(0))
    }

    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = userPagerState
    ){
            userIndex ->
        var user = storySessionUserList[userIndex]

        val storiesState = storySessionStoriesMap[user.userName]?: StoriesViewModel.StoriesListState()
        StoriesDetailPage(
            user = user,
            storiesState = storiesState,
            storiesViewModel = storiesViewModel,
            navController = navController,
            coroutineScope = coroutineScope,
            userPagerState = userPagerState
        )

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoriesDetailPage(
    storiesState: StoriesViewModel.StoriesListState,
    user: User,
    storiesViewModel: StoriesViewModel,
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    userPagerState: PagerState
){

    val storyPagerState = rememberPagerState(
        //0 when opening first time after opening the app. Kinda race condition
        initialPage = storiesState.storiesList.indexOfFirst {story->
            !story.viewed
        }.coerceAtLeast(0),
        pageCount = { storiesState.storiesList.size }
    )

    val currentStory = storiesState.storiesList.getOrNull(storyPagerState.currentPage)
    val progress = remember{ Animatable(initialValue = 0f) }

    val pagerIsDragged by storyPagerState.interactionSource.collectIsDraggedAsState()

    val pageInteractionSource = remember { MutableInteractionSource() }
    val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

    // Stop auto-advancing when pager is dragged or one of the pages is pressed
    val autoAdvance = !pagerIsDragged && !pageIsPressed

    val advanceStory = remember{
        {
                increment: Int ->
            coroutineScope.launch {
                if(storyPagerState.currentPage + increment in 0 until storiesState.storiesList.size){
                    storyPagerState.animateScrollToPage(storyPagerState.currentPage + increment)
                }else if (increment>0 && userPagerState.currentPage < userPagerState.pageCount - 1){
                    userPagerState.animateScrollToPage(userPagerState.currentPage+1)
                }else if (increment<0 && userPagerState.currentPage>0){
                    userPagerState.animateScrollToPage(userPagerState.currentPage-1)
                }else{
                    navController.navigateUp()
                }
            }

        }
    }

//     Reset progress when the current story changes
    LaunchedEffect(storyPagerState.currentPage) {
        progress.snapTo(0f) // Immediately set progress to 0
    }

    if (autoAdvance) {
        LaunchedEffect(storyPagerState, pageInteractionSource) {
            while (true) {
//                delay(2000)
//                val nextPage = (pagerState.currentPage + 1) % pageItems.size
//                pagerState.animateScrollToPage(nextPage)

                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (/*currentStory.timeLength * 1000L*/ 3*1000L).toInt(),
                        easing = LinearEasing
                    )
                ){
                    if (value == 1f){
                        advanceStory(1)
                    }
                }
//                delay(100)
            }
        }
    }

    //For auto advancement
//    var isPaused by remember{ mutableStateOf(false) }
//    val currentStory = storiesState.storiesList.getOrNull(storyPagerState.currentPage)
//    val progress = remember{ Animatable(initialValue = 0f) }

    ////Story Advancing
//    val advanceStory = remember{
//        {
//            increment: Int ->
//            coroutineScope.launch {
//                if(storyPagerState.currentPage + increment in 0 until storiesState.storiesList.size){
//                    storyPagerState.animateScrollToPage(storyPagerState.currentPage + increment)
//                }else if (increment>0 && userPagerState.currentPage < userPagerState.pageCount - 1){
//                    userPagerState.animateScrollToPage(userPagerState.currentPage+1)
//                }else if (increment<0 && userPagerState.currentPage>0){
//                    userPagerState.animateScrollToPage(userPagerState.currentPage-1)
//                }else{
//                    navController.navigateUp()
//                }
//            }
//
//        }
//    }

//    //Reset progress on new page. After animation-todo later
//    LaunchedEffect(storyPagerState.currentPage, storyPagerState.targetPage){
//        progress.snapTo(0f)
//        //TODO MARK AS VIEWED IF NOT already VIEWED
//    }
//
//    //Auto advance logic
//    LaunchedEffect(storyPagerState.currentPage, isPaused)/*currentStory?.timeLength)*/{
//        if (!isPaused && currentStory !=null){
//            progress.animateTo(
//                targetValue = 1f,
//                animationSpec = tween(
//                    durationMillis = (/*currentStory.timeLength * 1000L*/ 3*1000L).toInt(),
//                    easing = LinearEasing
//                )
//            ){
//                if(value ==1f){
//                    advanceStory(1)
//                }
//            }
//        }
////        else{
////            progress.stop()
////        }
//    }

//    LaunchedEffect(storyPagerState.currentPage,isPaused){
//        if(!isPaused && currentStory !=null){
//
//            progress.animateTo(
//                targetValue = 1f,
////                //from db
////                animationSpec = tween(durationMillis = (currentStory.timeLength * 1000L).toInt(), easing = LinearEasing)
//                animationSpec = tween(durationMillis = (3 * 1000L).toInt(), easing = LinearEasing)
//            )
//            if (progress.value==1f){
//                if (storyPagerState.currentPage < storyPagerState.pageCount-1) {
//                    storyPagerState.animateScrollToPage(storyPagerState.currentPage + 1)
//                } else if (userPagerState.currentPage < userPagerState.pageCount - 1) {
//                    userPagerState.animateScrollToPage(userPagerState.currentPage + 1)
//                } else {
//                    navController.navigateUp()
//                }
//
//                //TODO MARK AS VIEWED
//
//                progress.snapTo(0f)
//
//            }
//
//        }
//
//    }

    // Update viewed status of every settled page
//        LaunchedEffect(storyPagerState.currentPage){
//            println(storyPagerState.currentPage)
//            snapshotFlow {storyPagerState.currentPage}.collect{
//                val story = stories.getOrNull(storyPagerState.currentPage)
//                if (story != null && !story.viewed) {
//                    storiesViewModel.markStoryAsViewed(storyId = story.storyId)
//                }
//            }
//
//        }

    Scaffold(
        topBar = {
            StoriesTopBar(
            user.userName,
            storiesState.storiesList.size,
            storyPagerState.currentPage,
            timeProgress = progress.value
        ){
                navController.navigateUp() }
         },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        // Use a when statement to handle the different states of the stories data.
        when {

            storiesState.loading -> {
                ShimmerStory(paddingValues)
            }

            storiesState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${storiesState.error}")
                }
            }

            storiesState.storiesList.isNotEmpty() -> {
                StoriesPager(
                    stories = storiesState.storiesList,
                    storiesViewModel = storiesViewModel,
                    paddingValues = paddingValues,
                    coroutineScope = coroutineScope,
                    navController = navController,
                    userPagerState = userPagerState,
                    storyPagerState = storyPagerState,
                    pageInteractionSource = pageInteractionSource,
                    progress = progress
//                    onPause = { isPaused = true },
//                    onResume = { isPaused = false },
//                    onTapLeft = { advanceStory(-1) },
//                    onTapRight = { advanceStory(1) }
                )
            }

//            else -> {
//                // Handle the case where there are no stories.  This might not be an error,
//                // so we display a different message.
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text("No stories available for ${user.userName}.")
//                }
//            }
        }
    }

}

@Composable
fun StoriesPager(
    stories: List<Story>,
    storiesViewModel: StoriesViewModel,
    paddingValues: PaddingValues,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userPagerState: PagerState,
    storyPagerState: PagerState,
    pageInteractionSource: MutableInteractionSource,
    progress: Animatable<Float, AnimationVector1D>,
//    onPause: () -> Unit,
//    onResume: () -> Unit,
//    onTapLeft: () -> Unit,
//    onTapRight: () -> Unit
) {
    //temporary fix for above race
    LaunchedEffect(stories){
        storyPagerState.scrollToPage(stories.indexOfFirst {story->
            !story.viewed
        }.coerceAtLeast(0))
    }

//    LaunchedEffect(storyPagerState.currentPage) {
//        progress.snapTo(0f) // Immediately set progress to 0
//    }

    HorizontalPager(
        state = storyPagerState,
        modifier = Modifier
            .fillMaxSize()
//            .pointerInput(Unit){
//                               detectTapGestures(
//                                   onPress = {
//                                       onPause();
//                                       tryAwaitRelease();
//                                       onResume
//                                   },
//                                   onTap = {
//                                       offset ->
//                                       val screenWidth = size.width
//                                       if(offset.x < screenWidth/3){
//                                           onTapLeft()
//                                       }else if(offset.x > 2*screenWidth/3){
//                                           onTapRight
//                                       }
//                                   }
//                               )
//            }
        ,
        beyondViewportPageCount = 3,
        userScrollEnabled = false,

        ) {
            storyIndex->
        

        val currentStory = stories[storyIndex]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
//                .pointerInput(Unit){
//                    detectTapGestures(
//                        onPress = {
//                            onPause();
//                            tryAwaitRelease();
//                            onResume
//                        },
//                        onTap = {
//                                offset ->
//                            val screenWidth = size.width
//                            if(offset.x < screenWidth/3){
//                                onTapLeft()
//                            }else if(offset.x > 2*screenWidth/3){
//                                onTapRight
//                            }
//                        }
//                    )
//                }
//            ,
                .pointerInput(pageInteractionSource) {
                    detectTapGestures(
                        onPress = {
//                            onPause()
                            tryAwaitRelease()
//                            onResume()
                        },
                        onTap = { offset ->
                            val screenWidth = size.width
                            coroutineScope.launch {
                                if (offset.x < screenWidth / 3) {
                                    // Tap Left: Move to previous story or previous user
                                    if (storyPagerState.currentPage > 0) {
                                        storyPagerState.animateScrollToPage(storyPagerState.currentPage - 1)
                                    } else if (userPagerState.currentPage > 0) {
                                        userPagerState.animateScrollToPage(userPagerState.currentPage - 1)
                                    } else {
                                        navController.navigateUp()
                                    }
                                } else if (offset.x > 2 * screenWidth / 3) {
                                    // Tap Right: Move to next story or next user
                                    if (storyPagerState.currentPage < storyPagerState.pageCount - 1) {
                                        storyPagerState.animateScrollToPage(storyPagerState.currentPage + 1)
                                    } else if (userPagerState.currentPage < userPagerState.pageCount - 1) {
                                        userPagerState.animateScrollToPage(userPagerState.currentPage + 1)
                                    } else {
                                        navController.navigateUp()
                                    }
                                }
                            }
                        }
                    )

                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Divider(thickness = 2.dp, color = colorResource(id = R.color.orange_A200))

            Text(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp),
                text = currentStory.storyDetails,
                style = TextStyle(fontSize = 18.sp),
                textAlign = TextAlign.Left,
                lineHeight = 28.sp
            )

            Divider(thickness = 2.dp, color = colorResource(id = R.color.orange_A200))
            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoriesTopBar(name: String, storyCount: Int, storyIndex: Int,timeProgress:Float, onBackNavClicked:()->Unit={}){
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
//            .windowInsetsPadding(WindowInsets.statusBarsIgnoringVisibility)
    ){
//        Spacer(modifier = Modifier.height(4.dp))
        StoriesProgressIndicator(storyCount, storyIndex, timeProgress)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                        /*TODO the back button shouldn't go to previous story but to the storieslist screen or wherever they opened the stories detail from*/
                    onBackNavClicked()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Column(modifier = Modifier
                .weight(1f)
            ){
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.heightIn(max = 30.dp),
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    Text(text = "Yesterday", fontSize = 12.sp)
                    Text(text = ", ", fontSize = 12.sp)
                    Text(text = "10:45 pm", fontSize = 12.sp)
                }
            }

            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)){
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Report") },
                        onClick = { /* TODO: handle report */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Mute") },
                        onClick = { /* TODO: handle mute */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Block") },
                        onClick = { /* TODO: handle block */ }
                    )
                }
            }

        }
    }
}
@Composable
fun StoriesProgressIndicator(storyCount: Int, storyIndex: Int, timeProgress: Float) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        repeat(storyCount){
            index->
            val indicatorProgress = when{
                index < storyIndex ->1f
                index == storyIndex -> timeProgress
                else -> 0f
            }
            LinearProgressIndicator(
                progress = indicatorProgress,
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50)),
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                trackColor = Color.Gray.copy(alpha = 0.5f)

            )


//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(4.dp)
//                    .clip(RoundedCornerShape(50))
//                    .background(Color.Gray.copy(alpha = 0.3f))
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .fillMaxWidth(indicatorProgress)
//                        .background(if (isSystemInDarkTheme()) Color.White else Color.Black)
//                )
////                LinearProgressIndicator(
////                    progress = indicatorProgress,
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .height(4.dp)
////                        .clip(RoundedCornerShape(50)),
////                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
////                    trackColor = Color.Gray.copy(alpha = 0.5f)
//////                    backgroundColor = Color.Gray.copy(alpha = 0.5f),
////
////                )
//            }

        }
    }
}


//@Composable
//fun StoryDetailBottomBar(){
//
////    BottomAppBar(
////        modifier = Modifier
////            .fillMaxWidth(),
////        tonalElevation = 4.dp
////    ){
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp, horizontal = 12.dp)
//        ) {
//            TextField(
//                modifier = Modifier
//                    .fillMaxWidth()
////                .weight(1f)
//                    .height(48.dp)
//                    .clip(RoundedCornerShape(28.dp)),
//                value = "Reply",
//                onValueChange = {
//                    //TODO
//                },
//                textStyle = TextStyle(fontSize = 14.sp),
//                colors = TextFieldDefaults.colors(
//                    // making underline invisible
//                    disabledTextColor = Color.Transparent,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent
//                )
//            )
////        IconButton(
////            onClick = { /*TODO*/ },
////            modifier = Modifier
////                .weight(0.13f) // Increases space for the emoji
////                .padding(start = 4.dp)
////                .clip(RoundedCornerShape(100.dp))
////                .background(MaterialTheme.colorScheme.secondaryContainer)
////        ) {
////            Icon(
////                painter = painterResource(id = R.drawable.baseline_insert_emoticon_24),
////                contentDescription ="React",
////                modifier = Modifier.size(35.dp),
////                tint = MaterialTheme.colorScheme.onSecondaryContainer
////            )
////        }
//        }
//
//
////    }
//
//}


@Composable
fun ShimmerStory(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Divider(thickness = 2.dp, color = colorResource(id = R.color.orange_A200))
        Spacer(modifier = Modifier.height(24.dp))
        Column() {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .height(18.dp)
                .shimmerEffect())
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .height(18.dp)
                .shimmerEffect())
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .height(18.dp)
                .shimmerEffect())
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .height(18.dp)
                .shimmerEffect())
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .height(18.dp)
                .shimmerEffect())
            Box(modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .height(18.dp)
                .fillMaxWidth(fraction = 0.75f)
                .shimmerEffect())
        }
        Spacer(modifier = Modifier.height(24.dp))
        Divider(thickness = 2.dp, color = colorResource(id = R.color.orange_A200))
    }
}



@Preview
@Composable
fun topbarPreview(){
    StoriesTopBar(name = "Sajidha Abdulla", storyCount = 5, storyIndex =3 , timeProgress = 0.2f)
}