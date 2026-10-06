package org.noormahal.vp25.android.presentation.ui

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.common.CURRENT_USER_USERNAME
import org.noormahal.vp25.android.components.FabStyle
import org.noormahal.vp25.android.components.ShimmerStory
import org.noormahal.vp25.android.components.StoriesTopBar
import org.noormahal.vp25.android.components.StoryAnalyticsSheetContent
import org.noormahal.vp25.android.components.StoryContentCard
import org.noormahal.vp25.android.components.VpFab
import org.noormahal.vp25.android.data.Story
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel
import org.noormahal.vp25.android.theme.VpTheme
import java.util.Calendar
import java.util.Date

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

    val storySessionUserList by storiesViewModel.storySessionUsers.collectAsState()
    val userStoriesMap by storiesViewModel.userStoriesMap.collectAsState()


    if (storySessionUserList.isEmpty() || !storySessionUserList.all { userStoriesMap.containsKey(it.userName) }) {
        ShimmerStory(PaddingValues())
        return
    }

    val slides = remember(storySessionUserList, userStoriesMap) {
        storySessionUserList.flatMap { user ->
            val state = userStoriesMap[user.userName]
            // A user whose fetch failed contributes no slides - there's nothing
            // useful to show for them, and no retry UI exists today anyway.
            if (state == null || state.error != null) emptyList()
            else state.storiesList.map { story -> StorySlide(user, story) }
        }
    }

    if (slides.isEmpty()) {
        // Every session user's fetch failed - nothing to show.
        LaunchedEffect(Unit) { navController.navigateUp() }
        return
    }

    val initialIndex = remember(slides) {
        slides.indexOfFirst { it.user.userName == userName && !it.story.viewed }
            .let { if (it >= 0) it else slides.indexOfFirst { slide -> slide.user.userName == userName } }
            .coerceAtLeast(0)
    }

    StoriesDetailScreen(
        slides = slides,
        initialIndex = initialIndex,
        navController = navController,
        onStoryViewed = storiesViewModel::markStoryAsViewed
    )
}

private fun formatStoryTimePosted(context: Context, timePostedMillis: Long): String {
    val now = Calendar.getInstance()
    val posted = Calendar.getInstance().apply { timeInMillis = timePostedMillis }
    val yesterday = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }

    val dayLabel = when {
        isSameDay(posted, now) -> "Today"
        isSameDay(posted, yesterday) -> "Yesterday"
        // Stories only live 24h, so this shouldn't normally be reached.
        else -> android.text.format.DateFormat.getDateFormat(context).format(Date(timePostedMillis))
    }
    val time = android.text.format.DateFormat.getTimeFormat(context).format(Date(timePostedMillis))
    return "$dayLabel, $time"
}

private fun isSameDay(a: Calendar, b: Calendar): Boolean =
    a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun StoriesDetailScreen(
    slides: List<StorySlide>,
    initialIndex: Int,
    navController: NavHostController,
    onStoryViewed: (story: Story, reportToBackend: Boolean) -> Unit
) {
    val playerState = rememberStorySessionState(
        slides = slides,
        initialIndex = initialIndex,
        onExhausted = { navController.navigateUp() }
    )

    var showAnalyticsSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentSlide = slides[playerState.currentIndex]
    val isMyStory = currentSlide.user.userName == CURRENT_USER_USERNAME

    // Mark as settled-current slide viewed - guarded on the flag itself so resuming a
    // story you've already seen (e.g. swiping back to a user mid-session) doesn't write
    // the same update over and over. Only report to the backend for other users'
    // stories - there's nothing meaningful to report when you view your own.
    LaunchedEffect(playerState.currentIndex) {
        if (!currentSlide.story.viewed) {
            onStoryViewed(currentSlide.story, !isMyStory)
        }
    }

    val currentUserPos = playerState.userStartIndices.indexOfLast { it <= playerState.currentIndex }.coerceAtLeast(0)
    val userStart = playerState.userStartIndices[currentUserPos]
    val userEnd = playerState.userStartIndices.getOrElse(currentUserPos + 1) { slides.size }

    Scaffold(
        topBar = {
            StoriesTopBar(
                username = if (isMyStory) "Your Story" else currentSlide.user.displayName,
                timePosted = formatStoryTimePosted(context, currentSlide.story.timePosted),
                storyCount = userEnd - userStart,
                storyIndex = playerState.currentIndex - userStart,
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
                    onClick = {
                        showAnalyticsSheet = true
                        // Same pause mechanism as press-and-hold - the sheet covers the
                        // story, so auto-advance shouldn't keep running underneath it.
                        playerState.setPressed(true)
                    },
                    modifier = Modifier.padding(bottom = 24.dp),
                    style = FabStyle.ROUND_SECONDARY
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
    ) { paddingValues ->
        StorySlidesPager(
            slides = slides,
            paddingValues = paddingValues,
            playerState = playerState
        )
    }

    if (showAnalyticsSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAnalyticsSheet = false
                playerState.setPressed(false)
            }
        ) {
            StoryAnalyticsSheetContent()
        }
    }
}

@Composable
private fun StorySlidesPager(
    slides: List<StorySlide>,
    paddingValues: PaddingValues,
    playerState: StorySessionPlayerState
) {
    HorizontalPager(
        state = playerState.pagerState,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 1,
        userScrollEnabled = false,
    ) { index ->
        val slide = slides[index]
        StoryContentCard(
            storyDetails = slide.story.storyDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    val tapSlopPx = 18.dp.toPx()
                    val swipeUserThresholdPx = 56.dp.toPx()
                    val longPressThresholdMillis = 200L
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        playerState.setPressed(true)
                        var totalDragX = 0f
                        var isDrag = false
                        var lastChange = down
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            totalDragX += change.positionChange().x
                            if (!isDrag && kotlin.math.abs(totalDragX) > tapSlopPx) {
                                isDrag = true
                            }
                            if (isDrag) {
                                change.consume()
                            }
                            lastChange = change
                            if (!change.pressed) break
                        }
                        playerState.setPressed(false)
                        val heldMillis = lastChange.uptimeMillis - down.uptimeMillis
                        if (isDrag) {
                            when {
                                totalDragX > swipeUserThresholdPx -> playerState.jumpToUser(-1)
                                totalDragX < -swipeUserThresholdPx -> playerState.jumpToUser(1)
                            }
                        } else if (heldMillis < longPressThresholdMillis) {
                            val screenWidth = size.width
                            when {
                                down.position.x < screenWidth / 3 -> playerState.advance(-1)
                                down.position.x > 2 * screenWidth / 3 -> playerState.advance(1)
                            }
                        }
                    }
                }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StoriesDetailScreenPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val user = User("saji", "Sajidha Abdulla")
            StoriesDetailScreen(
                slides = listOf(
                    StorySlide(
                        user, Story(
                            userName = "saji",
                            displayName = "Sajidha Abdulla",
                            storyId = 1,
                            remoteId = "preview-1",
                            storyDetails = "Just got back from the best hike of my life. The view at the top was unreal.",
                            durationSeconds = 5,
                            timePosted = System.currentTimeMillis(),
                            viewed = true
                        )
                    ),
                    StorySlide(
                        user, Story(
                            userName = "saji",
                            displayName = "Sajidha Abdulla",
                            storyId = 2,
                            remoteId = "preview-2",
                            storyDetails = "One more thing!",
                            durationSeconds = 5,
                            timePosted = System.currentTimeMillis(),
                            viewed = false
                        )
                    )
                ),
                initialIndex = 1,
                navController = rememberNavController(),
                onStoryViewed = { _, _ -> }
            )
        }
    }
}
