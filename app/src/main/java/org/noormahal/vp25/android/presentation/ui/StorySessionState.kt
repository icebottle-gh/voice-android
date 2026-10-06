package org.noormahal.vp25.android.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import org.noormahal.vp25.android.data.Story
import org.noormahal.vp25.android.data.User


data class StorySlide(
    val user: User,
    val story: Story
)

@Stable
class StorySessionPlayerState internal constructor(
    val pagerState: PagerState,
    val currentIndex: Int,
    val userStartIndices: List<Int>,
    val progress: Animatable<Float, AnimationVector1D>,
    private val onAdvance: (increment: Int) -> Unit,
    private val onJumpToUser: (increment: Int) -> Unit,
    private val onPressedChange: (Boolean) -> Unit
) {
    fun advance(increment: Int) = onAdvance(increment)
    fun jumpToUser(increment: Int) = onJumpToUser(increment)
    fun setPressed(pressed: Boolean) = onPressedChange(pressed)
}


@Composable
fun rememberStorySessionState(
    slides: List<StorySlide>,
    initialIndex: Int,
    onExhausted: () -> Unit
): StorySessionPlayerState {
    val slideCount = slides.size

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { slideCount }
    )

    var currentIndex by remember { mutableIntStateOf(initialIndex) }

    val progress = remember { Animatable(initialValue = 0f) }

    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var pageIsPressed by remember { mutableStateOf(false) }
    val autoAdvance = !pagerIsDragged && !pageIsPressed

    var isTransitioning by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Where each user's own slides start, in flat-index terms - e.g. [0, 2, 5] means the
    // first user occupies indices 0-1, the second 2-4, the third 5+. Only recomputed if
    // the slide list itself changes (it's a frozen snapshot for the session - see
    // StoriesDetailView.kt - so in practice this runs once).
    val userStartIndices = remember(slides) {
        val starts = mutableListOf<Int>()
        var lastUserName: String? = null
        slides.forEachIndexed { index, slide ->
            if (slide.user.userName != lastUserName) {
                starts.add(index)
                lastUserName = slide.user.userName
            }
        }
        starts
    }


    val lastVisitedIndexForUser = remember {
        mutableMapOf(slides[initialIndex].user.userName to initialIndex)
    }

    fun landingIndexForUser(userStart: Int, userEnd: Int): Int {
        val userName = slides[userStart].user.userName
        val lastVisited = lastVisitedIndexForUser[userName]
        if (lastVisited != null && lastVisited in userStart until userEnd) return lastVisited
        // First time reaching this user this session - land on their first unviewed
        // story (per the static viewed flag from the DB), not literally their first
        // slide - otherwise a user with many stories means tapping all the way through
        // ones you've already seen just to reach the new one.
        return (userStart until userEnd).firstOrNull { !slides[it].story.viewed } ?: userStart
    }

    fun goTo(targetIndex: Int) {
        if (isTransitioning) return
        if (targetIndex !in 0 until slideCount) {
            onExhausted()
            return
        }
        isTransitioning = true
        coroutineScope.launch {
            try {
                progress.snapTo(0f)
                currentIndex = targetIndex
                lastVisitedIndexForUser[slides[targetIndex].user.userName] = targetIndex
                pagerState.animateScrollToPage(targetIndex)
            } finally {
                isTransitioning = false
            }
        }
    }

    fun jumpToUser(increment: Int) {
        val currentUserPos = userStartIndices.indexOfLast { it <= currentIndex }.coerceAtLeast(0)
        val targetUserPos = currentUserPos + increment
        if (targetUserPos !in userStartIndices.indices) {
            onExhausted()
            return
        }
        val targetUserStart = userStartIndices[targetUserPos]
        val targetUserEnd = userStartIndices.getOrElse(targetUserPos + 1) { slideCount }
        goTo(landingIndexForUser(targetUserStart, targetUserEnd))
    }

    fun advance(increment: Int) {
        val naiveTarget = currentIndex + increment
        if (naiveTarget !in 0 until slideCount) {
            onExhausted()
            return
        }
        if (slides[naiveTarget].user.userName == slides[currentIndex].user.userName) {
            goTo(naiveTarget)
        } else {
            jumpToUser(if (increment > 0) 1 else -1)
        }
    }

    if (autoAdvance) {
        LaunchedEffect(currentIndex) {
            val fullDurationMillis = slides[currentIndex].story.durationSeconds.coerceAtLeast(5) * 1000L
            val remainingDurationMillis = (fullDurationMillis * (1f - progress.value)).toLong()
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = remainingDurationMillis.toInt(),
                    easing = LinearEasing
                )
            )
            advance(1)
        }
    }

    return StorySessionPlayerState(
        pagerState = pagerState,
        currentIndex = currentIndex,
        userStartIndices = userStartIndices,
        progress = progress,
        onAdvance = ::advance,
        onJumpToUser = ::jumpToUser,
        onPressedChange = { pageIsPressed = it }
    )
}
