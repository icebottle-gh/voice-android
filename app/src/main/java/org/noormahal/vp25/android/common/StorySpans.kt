package org.noormahal.vp25.android.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/** A half-open character range [start, end) into some text - the unit this whole file
 * works in for tracking which characters have bold/italic/strikethrough applied. */
data class Span(val start: Int, val end: Int)

/** Union [new] into [spans], merging with anything it overlaps or touches so the set
 * stays a minimal non-overlapping collection of ranges. */
fun mergeAdd(spans: List<Span>, new: Span): List<Span> {
    if (new.start >= new.end) return spans
    val merged = mutableListOf<Span>()
    for (span in (spans + new).sortedBy { it.start }) {
        val last = merged.lastOrNull()
        if (last != null && span.start <= last.end) {
            merged[merged.lastIndex] = Span(last.start, maxOf(last.end, span.end))
        } else {
            merged.add(span)
        }
    }
    return merged
}

/** Subtracts [remove] from every range in [spans], splitting a range that only
 * partially overlaps it rather than dropping the whole thing. */
fun subtract(spans: List<Span>, remove: Span): List<Span> {
    if (remove.start >= remove.end) return spans
    val result = mutableListOf<Span>()
    for (span in spans) {
        if (span.end <= remove.start || span.start >= remove.end) {
            result.add(span)
            continue
        }
        if (span.start < remove.start) result.add(Span(span.start, remove.start))
        if (span.end > remove.end) result.add(Span(remove.end, span.end))
    }
    return result
}

/** Whether every character in [range] is covered by at least one range in [spans] -
 * used to decide whether toggling a format on a selection should turn it on or off. */
fun isFullyCovered(spans: List<Span>, range: Span): Boolean {
    if (range.start >= range.end) return false
    var covered = range.start
    for (span in spans.filter { it.end > range.start && it.start < range.end }.sortedBy { it.start }) {
        if (span.start > covered) return false
        covered = maxOf(covered, span.end)
        if (covered >= range.end) return true
    }
    return covered >= range.end
}

/** A text-field edit expressed as "these old characters [deleteStart, deleteEnd) were
 * replaced by this many new characters", the minimal diff needed to shift [Span]s. */
data class TextEdit(val deleteStart: Int, val deleteEnd: Int, val insertLength: Int)

/** Diffs [old] against [new] by trimming their common prefix/suffix, leaving just the
 * edited middle - BasicTextField only ever hands back the resulting full string, not
 * what changed, so this is how that gets recovered. */
fun computeTextEdit(old: String, new: String): TextEdit {
    val maxPrefix = minOf(old.length, new.length)
    var prefix = 0
    while (prefix < maxPrefix && old[prefix] == new[prefix]) prefix++

    val maxSuffix = maxPrefix - prefix
    var suffix = 0
    while (suffix < maxSuffix && old[old.length - 1 - suffix] == new[new.length - 1 - suffix]) suffix++

    val deleteStart = prefix
    val deleteEnd = old.length - suffix
    val insertEnd = new.length - suffix
    return TextEdit(deleteStart, deleteEnd, insertEnd - prefix)
}

/** Adjusts [spans] for a [TextEdit]: clips/drops whatever fell inside the deleted
 * range, then shifts everything after it by however much the text grew or shrank. */
fun shiftForEdit(spans: List<Span>, deleteStart: Int, deleteEnd: Int, insertLength: Int): List<Span> {
    val afterDelete = subtract(spans, Span(deleteStart, deleteEnd))
    val netShift = insertLength - (deleteEnd - deleteStart)
    return afterDelete
        .map { span -> if (span.start >= deleteEnd) Span(span.start + netShift, span.end + netShift) else span }
        .filter { it.start < it.end }
}

fun boldSpansOf(annotated: AnnotatedString): List<Span> =
    annotated.spanStyles.filter { it.item.fontWeight == FontWeight.Bold }.map { Span(it.start, it.end) }

fun italicSpansOf(annotated: AnnotatedString): List<Span> =
    annotated.spanStyles.filter { it.item.fontStyle == FontStyle.Italic }.map { Span(it.start, it.end) }

fun strikethroughSpansOf(annotated: AnnotatedString): List<Span> =
    annotated.spanStyles.filter { it.item.textDecoration == TextDecoration.LineThrough }.map { Span(it.start, it.end) }

/** Combines the three independently-tracked format range lists into the
 * [AnnotatedString.Range]s Compose's rendering APIs (addStyle, visual transformations)
 * actually take. */
fun toDisplaySpans(
    boldSpans: List<Span>,
    italicSpans: List<Span>,
    strikethroughSpans: List<Span>
): List<AnnotatedString.Range<SpanStyle>> =
    boldSpans.map { AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), it.start, it.end) } +
        // fontSynthesis is explicit (not just relying on the TextStyle default) since
        // italic otherwise rendering as a no-op has been reported - this forces slant
        // synthesis even if the resolved typeface's own metadata is what's skipping it.
        italicSpans.map {
            AnnotatedString.Range(SpanStyle(fontStyle = FontStyle.Italic, fontSynthesis = FontSynthesis.Style), it.start, it.end)
        } +
        strikethroughSpans.map { AnnotatedString.Range(SpanStyle(textDecoration = TextDecoration.LineThrough), it.start, it.end) }
