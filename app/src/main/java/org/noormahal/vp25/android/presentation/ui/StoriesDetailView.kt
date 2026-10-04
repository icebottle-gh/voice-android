package org.noormahal.vp25.android.presentation.ui

import android.app.Activity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.components.FabStyle
import org.noormahal.vp25.android.components.ShimmerStory
import org.noormahal.vp25.android.components.StoriesTopBar
import org.noormahal.vp25.android.components.StoryAnalyticsSheetContent
import org.noormahal.vp25.android.components.StoryContentCard
import org.noormahal.vp25.android.components.VpFab
import org.noormahal.vp25.android.data.Story
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel
import kotlinx.coroutines.CoroutineScope


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

    // dummy data for now, pending real backend wiring
    // (storiesViewModel.startStorySession() above already populates storySessionUsers/userStoriesMap for real)
    val storySessionUserList = listOf(
        User("saji", "Sajidha Abdulla", true),
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
            // TODO: replace with the real current-user id once auth/session state is wired here
            isMyStory = user.userName == CURRENT_USER_USERNAME,
            storiesState = storiesState,
            storiesViewModel = storiesViewModel,
            navController = navController,
            coroutineScope = coroutineScope,
            userPagerState = userPagerState
        )

    }

}

const val CURRENT_USER_USERNAME = "saji"

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StoriesDetailPage(
    storiesState: StoriesViewModel.StoriesListState,
    user: User,
    isMyStory: Boolean,
    storiesViewModel: StoriesViewModel,
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    userPagerState: PagerState
){

    val playerState = rememberStoryPlayerState(
        storyCount = storiesState.storiesList.size,
        initialPage = storiesState.storiesList.indexOfFirst { story -> !story.viewed }.coerceAtLeast(0),
        userPagerState = userPagerState,
        coroutineScope = coroutineScope,
        onExhausted = { navController.navigateUp() }
    )

    // TODO: mark the settled story as viewed once real data wiring lands (storiesViewModel.markStoryAsViewed)

    var showAnalyticsSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            StoriesTopBar(
                username = user.userName,
                timePosted = "Yesterday, 10:45 pm",
                storyCount = storiesState.storiesList.size,
                storyIndex = playerState.storyPagerState.currentPage,
                timeProgress = playerState.progress.value
            ) {
                navController.navigateUp()
            }
         },
        // TODO: uncomment once replying to a story has backend support
        // bottomBar = {
        //     if (!isMyStory) {
        //         var replyText by remember { mutableStateOf("") }
        //         StoryReplyInput(value = replyText, onValueChange = { replyText = it })
        //     }
        // },
        floatingActionButton = {
            if (isMyStory) {
                VpFab(
                    icon = ImageVector.vectorResource(id = R.drawable.outline_bar_chart_24),
                    contentDescription = "View story analytics",
                    onClick = { showAnalyticsSheet = true },
                    modifier = Modifier.padding(bottom = 24.dp),
                    style = FabStyle.ROUND_SECONDARY
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
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
                    paddingValues = paddingValues,
                    storyPagerState = playerState.storyPagerState,
                    advanceStory = playerState::advanceStory,
                    onPressedChange = playerState::setPressed
                )
            }

            // TODO: empty state (user has zero stories) - currently renders nothing
        }
    }

    if (showAnalyticsSheet) {
        ModalBottomSheet(onDismissRequest = { showAnalyticsSheet = false }) {
            StoryAnalyticsSheetContent()
        }
    }

}

@Composable
fun StoriesPager(
    stories: List<Story>,
    paddingValues: PaddingValues,
    storyPagerState: PagerState,
    advanceStory: (Int) -> Unit,
    onPressedChange: (Boolean) -> Unit,
) {
    //temporary fix for above race
    LaunchedEffect(stories){
        storyPagerState.scrollToPage(stories.indexOfFirst {story->
            !story.viewed
        }.coerceAtLeast(0))
    }

    HorizontalPager(
        state = storyPagerState,
        modifier = Modifier
            .fillMaxSize(),
        beyondViewportPageCount = 3,
        userScrollEnabled = false,
    ) {
            storyIndex->

        val currentStory = stories[storyIndex]
        StoryContentCard(
            storyDetails = currentStory.storyDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onPressedChange(true)
                            tryAwaitRelease()
                            onPressedChange(false)
                        },
                        onTap = { offset ->
                            val screenWidth = size.width
                            when {
                                // Tap left: previous story, bleeding into the previous user if needed
                                offset.x < screenWidth / 3 -> advanceStory(-1)
                                // Tap right: next story, bleeding into the next user if needed
                                offset.x > 2 * screenWidth / 3 -> advanceStory(1)
                            }
                        }
                    )
                }
        )
    }
}
