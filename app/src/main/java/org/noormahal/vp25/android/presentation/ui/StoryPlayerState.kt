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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Stable
class StoryPlayerState internal constructor(
    val storyPagerState: PagerState,
    val progress: Animatable<Float, AnimationVector1D>,
    val autoAdvance: Boolean,
    private val onAdvance: (increment: Int) -> Unit,
    private val onPressedChange: (Boolean) -> Unit
) {
    fun advanceStory(increment: Int) = onAdvance(increment)
    fun setPressed(pressed: Boolean) = onPressedChange(pressed)
}

@Composable
fun rememberStoryPlayerState(
    storyCount: Int,
    initialPage: Int,
    userPagerState: PagerState,
    coroutineScope: CoroutineScope,
    onExhausted: () -> Unit
): StoryPlayerState {
    val storyPagerState = rememberPagerState(
        //0 when opening first time after opening the app. Kinda race condition
        initialPage = initialPage,
        pageCount = { storyCount }
    )

    val progress = remember { Animatable(initialValue = 0f) }

    val pagerIsDragged by storyPagerState.interactionSource.collectIsDraggedAsState()

    var pageIsPressed by remember { mutableStateOf(false) }

    // Stop auto-advancing when pager is dragged or one of the pages is pressed
    val autoAdvance = !pagerIsDragged && !pageIsPressed

    val advanceStory: (Int) -> Unit = remember {
        { increment: Int ->
            coroutineScope.launch {
                if (storyPagerState.currentPage + increment in 0 until storyCount) {
                    storyPagerState.animateScrollToPage(storyPagerState.currentPage + increment)
                } else if (increment > 0 && userPagerState.currentPage < userPagerState.pageCount - 1) {
                    userPagerState.animateScrollToPage(userPagerState.currentPage + 1)
                } else if (increment < 0 && userPagerState.currentPage > 0) {
                    userPagerState.animateScrollToPage(userPagerState.currentPage - 1)
                } else {
                    onExhausted()
                }
            }
        }
    }

    // Reset progress when the current story changes
    LaunchedEffect(storyPagerState.currentPage) {
        progress.snapTo(0f)
    }

    if (autoAdvance) {
        LaunchedEffect(storyPagerState) {
            while (true) {
                // TODO: use the story's actual duration once Story models one; hardcoded for now
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (3 * 1000L).toInt(),
                        easing = LinearEasing
                    )
                ) {
                    if (value == 1f) {
                        advanceStory(1)
                    }
                }
            }
        }
    }

    return StoryPlayerState(
        storyPagerState = storyPagerState,
        progress = progress,
        autoAdvance = autoAdvance,
        onAdvance = advanceStory,
        onPressedChange = { pageIsPressed = it }
    )
}
