package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.common.applySpans
import org.noormahal.vp25.android.theme.StoryContentTextStyle
import org.noormahal.vp25.android.theme.VpTheme

/**
 * Compose/edit screen for a new story - empty placeholder state and actively-typing
 * state are the same screen, just differing in [fieldValue] and keyboard focus.
 *
 * Formatting is per-character-range rather than whole-story: [renderedSpans] carries
 * whatever bold/italic/strikethrough ranges the caller has built up (from a selection,
 * or from toggling a format on before typing), and is painted onto the field via a
 * [VisualTransformation] rather than baked into a single [androidx.compose.ui.text.TextStyle] for
 * the whole field.
 */
@Composable
fun NewStoryScreen(
    fieldValue: TextFieldValue,
    onFieldValueChange: (TextFieldValue) -> Unit,
    renderedSpans: List<AnnotatedString.Range<SpanStyle>>,
    isBoldActive: Boolean,
    isItalicActive: Boolean,
    isStrikethroughActive: Boolean,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleStrikethrough: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            VpTopAppBar(
                title = "Add New Story",
                onLeadingIconClick = onBackClick,
                showTrailingIcon = false
            )
        },
        floatingActionButton = {
            if (fieldValue.text.isNotBlank()) {
                VpFab(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = "Next",
                    onClick = onNextClick,
                    style = FabStyle.ROUND_PRIMARY
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (fieldValue.text.isEmpty()) {
                    Text(
                        text = "Write Something",
                        style = StoryContentTextStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
                val visualTransformation = remember(renderedSpans) {
                    VisualTransformation { text ->
                        TransformedText(applySpans(text.text, renderedSpans), OffsetMapping.Identity)
                    }
                }
                BasicTextField(
                    value = fieldValue,
                    onValueChange = onFieldValueChange,
                    visualTransformation = visualTransformation,
                    textStyle = StoryContentTextStyle.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    // Explicit rather than relying on the default - ImeAction.None keeps
                    // the keyboard's return key inserting a newline instead of a keyboard
                    // potentially treating it as a "Done"/submit action.
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            FormattingToolbar(
                isBold = isBoldActive,
                isItalic = isItalicActive,
                isStrikethrough = isStrikethroughActive,
                onToggleBold = onToggleBold,
                onToggleItalic = onToggleItalic,
                onToggleStrikethrough = onToggleStrikethrough,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun FormattingToolbar(
    isBold: Boolean,
    isItalic: Boolean,
    isStrikethrough: Boolean,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleStrikethrough: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        VpIconToggleButton(
            icon = R.drawable.baseline_format_bold_24,
            selected = isBold,
            onToggle = onToggleBold,
            contentDescription = "Bold"
        )
        VpIconToggleButton(
            icon = R.drawable.baseline_format_italic_24,
            selected = isItalic,
            onToggle = onToggleItalic,
            contentDescription = "Italic"
        )
        VpIconToggleButton(
            icon = R.drawable.baseline_format_strikethrough_24,
            selected = isStrikethrough,
            onToggle = onToggleStrikethrough,
            contentDescription = "Strikethrough"
        )
    }
}

/**
 * Final preview before posting - read-only rendering of the composed story text with
 * [spans] applied, same per-range formatting as the compose screen.
 */
@Composable
fun NewStoryPreviewScreen(
    text: String,
    spans: List<AnnotatedString.Range<SpanStyle>>,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean = false,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            VpTopAppBar(
                title = "Preview",
                onLeadingIconClick = onBackClick,
                showTrailingIcon = false
            )
        },
        floatingActionButton = {
            VpFab(
                icon = ImageVector.vectorResource(id = R.drawable.baseline_send_24),
                contentDescription = "Post story",
                onClick = { if (!isSending) onSendClick() },
                style = FabStyle.ROUND_PRIMARY,
                modifier = Modifier.alpha(if (isSending) 0.6f else 1f)
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = applySpans(text, spans),
                style = StoryContentTextStyle.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Shown when leaving an in-progress story unsent - "Delete" discards it, "Save" keeps
 * it as a draft.
 */
@Composable
fun SaveStoryAsDraftDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    VpDialog(
        onDismissRequest = onDismissRequest,
        title = "Save as Draft?",
        buttons = listOf(
            VpDialogButtonSpec(text = "Delete", onClick = onDelete, style = ButtonStyle.ROUND_PRIMARY_OUTLINE),
            VpDialogButtonSpec(text = "Save", onClick = onSave, style = ButtonStyle.ROUND_PRIMARY)
        ),
        modifier = modifier
    )
}

@Preview(name = "Light - Empty", showBackground = true, widthDp = 412, heightDp = 915)
@Preview(name = "Dark - Empty", showBackground = true, widthDp = 412, heightDp = 915, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewStoryScreenEmptyPreview() {
    VpTheme {
        var fieldValue by remember { mutableStateOf(TextFieldValue("")) }
        NewStoryScreen(
            fieldValue = fieldValue,
            onFieldValueChange = { fieldValue = it },
            renderedSpans = emptyList(),
            isBoldActive = false,
            isItalicActive = false,
            isStrikethroughActive = false,
            onToggleBold = {},
            onToggleItalic = {},
            onToggleStrikethrough = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}

// "Hello" bold, "There" italic, the rest plain - demonstrates formatting applying to
// just a few words rather than the whole story.
private val TYPING_PREVIEW_SPANS = listOf(
    AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), 0, 5),
    AnnotatedString.Range(SpanStyle(fontStyle = FontStyle.Italic), 6, 11)
)

@Preview(name = "Light - Typing", showBackground = true, widthDp = 412, heightDp = 915)
@Preview(name = "Dark - Typing", showBackground = true, widthDp = 412, heightDp = 915, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewStoryScreenTypingPreview() {
    VpTheme {
        var fieldValue by remember { mutableStateOf(TextFieldValue("Hello There!")) }
        NewStoryScreen(
            fieldValue = fieldValue,
            onFieldValueChange = { fieldValue = it },
            renderedSpans = TYPING_PREVIEW_SPANS,
            isBoldActive = false,
            isItalicActive = false,
            isStrikethroughActive = false,
            onToggleBold = {},
            onToggleItalic = {},
            onToggleStrikethrough = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}

@Preview(name = "Light - Multiline", showBackground = true, widthDp = 412, heightDp = 915)
@Preview(name = "Dark - Multiline", showBackground = true, widthDp = 412, heightDp = 915, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewStoryScreenMultilinePreview() {
    VpTheme {
        val initialText = "Hello There!\nSecond line.\n\nThird, after a blank line."
        var fieldValue by remember { mutableStateOf(TextFieldValue(initialText)) }
        NewStoryScreen(
            fieldValue = fieldValue,
            onFieldValueChange = { fieldValue = it },
            renderedSpans = emptyList(),
            isBoldActive = false,
            isItalicActive = false,
            isStrikethroughActive = false,
            onToggleBold = {},
            onToggleItalic = {},
            onToggleStrikethrough = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 412, heightDp = 915)
@Preview(name = "Dark", showBackground = true, widthDp = 412, heightDp = 915, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewStoryPreviewScreenPreview() {
    VpTheme {
        NewStoryPreviewScreen(
            text = "Hello There",
            spans = TYPING_PREVIEW_SPANS,
            onBackClick = {},
            onSendClick = {}
        )
    }
}
