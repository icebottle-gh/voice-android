package org.noormahal.vp25.android.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

// The backend only ever stores/returns a plain String (see Story.text in the vakkic
// client), with no concept of rich text - so bold/italic/strikethrough are encoded as
// markdown-style markers directly in that string (**bold**, *italic*, ~~strike~~) and
// decoded back into an AnnotatedString wherever a story is rendered. Formatting is
// per-character-range (see StorySpans.kt for how those ranges are built/edited), not
// all-or-nothing for the whole story.
//
// A literal '*', '~' or '\' typed by the user is backslash-escaped on the way in and
// un-escaped on the way out, so someone who actually wants to type "**" sees "**", not
// a formatting toggle.

/** Applies [spans] over [text] for live rendering (editing preview, send preview) -
 * doesn't touch the backend wire format, see [annotatedStringToMarkup] for that. */
fun applySpans(text: String, spans: List<AnnotatedString.Range<SpanStyle>>): AnnotatedString =
    buildAnnotatedString {
        append(text)
        spans.forEach { addStyle(it.item, it.start, it.end) }
    }

/** Encodes [annotated] into the markup wire format - the inverse of [parseStoryMarkup].
 * Walks the text character by character, emitting a toggle marker wherever the active
 * bold/italic/strikethrough state changes, so arbitrary per-range combinations (a single
 * word, overlapping ranges, whatever) round-trip correctly rather than only a
 * whole-string wrap. */
fun annotatedStringToMarkup(annotated: AnnotatedString): String {
    val text = annotated.text
    val spans = annotated.spanStyles

    fun stateAt(index: Int): Triple<Boolean, Boolean, Boolean> {
        var bold = false
        var italic = false
        var strikethrough = false
        for (span in spans) {
            if (index >= span.start && index < span.end) {
                if (span.item.fontWeight == FontWeight.Bold) bold = true
                if (span.item.fontStyle == FontStyle.Italic) italic = true
                if (span.item.textDecoration == TextDecoration.LineThrough) strikethrough = true
            }
        }
        return Triple(bold, italic, strikethrough)
    }

    val builder = StringBuilder()
    var curBold = false
    var curItalic = false
    var curStrikethrough = false

    for (i in text.indices) {
        val (bold, italic, strikethrough) = stateAt(i)
        if (bold != curBold) {
            builder.append("**")
            curBold = bold
        }
        if (italic != curItalic) {
            builder.append("*")
            curItalic = italic
        }
        if (strikethrough != curStrikethrough) {
            builder.append("~~")
            curStrikethrough = strikethrough
        }
        val ch = text[i]
        if (ch == '\\' || ch == '*' || ch == '~') builder.append('\\')
        builder.append(ch)
    }
    if (curBold) builder.append("**")
    if (curItalic) builder.append("*")
    if (curStrikethrough) builder.append("~~")
    return builder.toString()
}

/** Parses markup produced by [annotatedStringToMarkup] back into styled spans. Markers
 * are simple on/off toggles, so this also tolerates non-nested or interleaved markers
 * rather than assuming well-formed nesting. A backslash escapes the character after it
 * so it's appended literally instead of toggling a style. */
fun parseStoryMarkup(markup: String): AnnotatedString = buildAnnotatedString {
    var bold = false
    var italic = false
    var strikethrough = false
    var runStart = 0

    fun flushRun() {
        if (length > runStart) {
            addStyle(
                SpanStyle(
                    fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                    // Explicit rather than relying on the default - forces slant
                    // synthesis even if the resolved typeface's own metadata skips it.
                    fontSynthesis = FontSynthesis.Style,
                    textDecoration = if (strikethrough) TextDecoration.LineThrough else TextDecoration.None
                ),
                runStart,
                length
            )
        }
        runStart = length
    }

    var i = 0
    while (i < markup.length) {
        when {
            markup[i] == '\\' && i + 1 < markup.length -> {
                append(markup[i + 1])
                i += 2
            }
            markup.startsWith("**", i) -> {
                flushRun()
                bold = !bold
                i += 2
            }
            markup[i] == '*' -> {
                flushRun()
                italic = !italic
                i += 1
            }
            markup.startsWith("~~", i) -> {
                flushRun()
                strikethrough = !strikethrough
                i += 2
            }
            else -> {
                append(markup[i])
                i += 1
            }
        }
    }
    flushRun()
}
