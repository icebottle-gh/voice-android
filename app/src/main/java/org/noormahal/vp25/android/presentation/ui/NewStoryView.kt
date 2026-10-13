package org.noormahal.vp25.android.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.noormahal.vp25.android.common.Span
import org.noormahal.vp25.android.common.annotatedStringToMarkup
import org.noormahal.vp25.android.common.applySpans
import org.noormahal.vp25.android.common.boldSpansOf
import org.noormahal.vp25.android.common.computeTextEdit
import org.noormahal.vp25.android.common.isFullyCovered
import org.noormahal.vp25.android.common.italicSpansOf
import org.noormahal.vp25.android.common.mergeAdd
import org.noormahal.vp25.android.common.parseStoryMarkup
import org.noormahal.vp25.android.common.shiftForEdit
import org.noormahal.vp25.android.common.strikethroughSpansOf
import org.noormahal.vp25.android.common.subtract
import org.noormahal.vp25.android.common.toDisplaySpans
import org.noormahal.vp25.android.components.NewStoryPreviewScreen
import org.noormahal.vp25.android.components.NewStoryScreen
import org.noormahal.vp25.android.components.SaveStoryAsDraftDialog
import org.noormahal.vp25.android.data.Draft
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel

private enum class NewStoryStep { Compose, Preview }

/** Owns the compose -> preview -> send flow as one nav destination - the two Figma
 * frames are steps within it rather than separate routes, so "back" from Preview
 * returns to Compose instead of leaving the flow.
 *
 * Formatting is tracked per-character-range (bold/italic/strikethrough each as their
 * own independent, mergeable [Span] list over [fieldValue]'s text) rather than one
 * whole-story toggle - toggling a format with a selection applies to just that
 * selection; toggling with a collapsed cursor (no selection) flips a "pending" flag
 * that tags whatever gets typed next, until toggled off again. */
@Composable
fun NewStoryView(navController: NavHostController, storiesViewModel: StoriesViewModel) {
    var step by remember { mutableStateOf(NewStoryStep.Compose) }
    var fieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var boldSpans by remember { mutableStateOf(listOf<Span>()) }
    var italicSpans by remember { mutableStateOf(listOf<Span>()) }
    var strikethroughSpans by remember { mutableStateOf(listOf<Span>()) }
    var pendingBold by remember { mutableStateOf(false) }
    var pendingItalic by remember { mutableStateOf(false) }
    var pendingStrikethrough by remember { mutableStateOf(false) }
    var showDraftDialog by remember { mutableStateOf(false) }
    var isSending by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Resume the single saved draft, if any - its markup is parsed back into ranges
    // so it's editable the same way a freshly-typed story would be.
    LaunchedEffect(Unit) {
        storiesViewModel.getDraft()?.let { draft ->
            val parsed = parseStoryMarkup(draft.text)
            fieldValue = TextFieldValue(parsed.text)
            boldSpans = boldSpansOf(parsed)
            italicSpans = italicSpansOf(parsed)
            strikethroughSpans = strikethroughSpansOf(parsed)
        }
    }

    fun onFieldValueChange(new: TextFieldValue) {
        val old = fieldValue.text
        if (old != new.text) {
            val edit = computeTextEdit(old, new.text)
            boldSpans = shiftForEdit(boldSpans, edit.deleteStart, edit.deleteEnd, edit.insertLength)
            italicSpans = shiftForEdit(italicSpans, edit.deleteStart, edit.deleteEnd, edit.insertLength)
            strikethroughSpans = shiftForEdit(strikethroughSpans, edit.deleteStart, edit.deleteEnd, edit.insertLength)
            if (edit.insertLength > 0) {
                val inserted = Span(edit.deleteStart, edit.deleteStart + edit.insertLength)
                if (pendingBold) boldSpans = mergeAdd(boldSpans, inserted)
                if (pendingItalic) italicSpans = mergeAdd(italicSpans, inserted)
                if (pendingStrikethrough) strikethroughSpans = mergeAdd(strikethroughSpans, inserted)
            }
        }
        fieldValue = new
    }

    val selection = fieldValue.selection.takeUnless { it.collapsed }
        ?.let { Span(minOf(it.start, it.end), maxOf(it.start, it.end)) }

    // With a selection, toggling applies to exactly that selection right now; with
    // just a cursor, toggling instead flips the "pending" flag for whatever's typed next.
    fun toggle(spans: List<Span>, setSpans: (List<Span>) -> Unit, pending: Boolean, setPending: (Boolean) -> Unit) {
        if (selection != null) {
            setSpans(if (isFullyCovered(spans, selection)) subtract(spans, selection) else mergeAdd(spans, selection))
        } else {
            setPending(!pending)
        }
    }

    // Toolbar buttons reflect the selection's current formatting when there is one,
    // otherwise the pending flag that'll apply to the next character typed.
    val isBoldActive = selection?.let { isFullyCovered(boldSpans, it) } ?: pendingBold
    val isItalicActive = selection?.let { isFullyCovered(italicSpans, it) } ?: pendingItalic
    val isStrikethroughActive = selection?.let { isFullyCovered(strikethroughSpans, it) } ?: pendingStrikethrough

    val displaySpans = remember(boldSpans, italicSpans, strikethroughSpans) {
        toDisplaySpans(boldSpans, italicSpans, strikethroughSpans)
    }

    fun handleBack() {
        when {
            isSending -> Unit // ignore back while a send is in flight
            step == NewStoryStep.Preview -> step = NewStoryStep.Compose
            fieldValue.text.isNotBlank() -> showDraftDialog = true
            else -> navController.navigateUp()
        }
    }

    BackHandler(onBack = ::handleBack)

    when (step) {
        NewStoryStep.Compose -> NewStoryScreen(
            fieldValue = fieldValue,
            onFieldValueChange = ::onFieldValueChange,
            renderedSpans = displaySpans,
            isBoldActive = isBoldActive,
            isItalicActive = isItalicActive,
            isStrikethroughActive = isStrikethroughActive,
            onToggleBold = { toggle(boldSpans, { boldSpans = it }, pendingBold) { pendingBold = it } },
            onToggleItalic = { toggle(italicSpans, { italicSpans = it }, pendingItalic) { pendingItalic = it } },
            onToggleStrikethrough = {
                toggle(strikethroughSpans, { strikethroughSpans = it }, pendingStrikethrough) { pendingStrikethrough = it }
            },
            onBackClick = ::handleBack,
            onNextClick = { step = NewStoryStep.Preview }
        )

        NewStoryStep.Preview -> NewStoryPreviewScreen(
            text = fieldValue.text,
            spans = displaySpans,
            onBackClick = ::handleBack,
            isSending = isSending,
            onSendClick = {
                // Guards against a double-tap firing postStory() twice and posting the
                // same story twice.
                if (!isSending) {
                    isSending = true
                    coroutineScope.launch {
                        val markup = annotatedStringToMarkup(applySpans(fieldValue.text, displaySpans))
                        if (storiesViewModel.postStory(markup)) {
                            navController.navigateUp()
                        } else {
                            isSending = false
                            // TODO: surface a failure message - postStory() returning
                            // false just leaves you on Preview today with no feedback.
                        }
                    }
                }
            }
        )
    }

    if (showDraftDialog) {
        SaveStoryAsDraftDialog(
            onDismissRequest = { showDraftDialog = false },
            onDelete = {
                coroutineScope.launch {
                    storiesViewModel.clearDraft()
                    navController.navigateUp()
                }
            },
            onSave = {
                coroutineScope.launch {
                    val markup = annotatedStringToMarkup(applySpans(fieldValue.text, displaySpans))
                    storiesViewModel.saveDraft(Draft(text = markup))
                    navController.navigateUp()
                }
            }
        )
    }
}
