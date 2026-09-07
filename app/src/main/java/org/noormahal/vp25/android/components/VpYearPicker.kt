package org.noormahal.vp25.android.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar
import kotlin.math.abs
import org.noormahal.vp25.android.theme.VpTheme


private const val DEFAULT_MIN_YEAR = 1940
private val WHEEL_ITEM_HEIGHT = 48.dp
private const val WHEEL_VISIBLE_ITEMS = 5
private val WHEEL_WIDTH = 120.dp

private fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VpYearPicker(
    value: Int?,
    onValueChange: (Int) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    range: IntRange = DEFAULT_MIN_YEAR..currentYear(),
) {
    var showDialog by remember { mutableStateOf(false) }

    VpTextField(
        value = value?.toString() ?: "",
        onValueChange = {},
        label = label,
        modifier = modifier
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDialog = true
                    }
                }
            },
        readOnly = true,
        leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
    )

    if (showDialog) {
        var pendingValue by remember { mutableIntStateOf(value ?: range.last) }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange(pendingValue)
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    YearWheel(
                        value = pendingValue,
                        range = range,
                        onValueChange = { pendingValue = it },
                    )
                }
            },
        )
    }
}

@Composable
private fun YearWheel(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
) {
    val years = remember(range) { range.toList() }
    val initialIndex = remember(value, years) { years.indexOf(value).coerceAtLeast(0) }
    val listState = rememberLazyListState(initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState)

    val centeredIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { info ->
                abs((info.offset + info.size / 2) - viewportCenter)
            }?.index ?: initialIndex
        }
    }

    LaunchedEffect(centeredIndex) {
        years.getOrNull(centeredIndex)?.let(onValueChange)
    }

    Box(
        modifier = Modifier
            .width(WHEEL_WIDTH)
            .height(WHEEL_ITEM_HEIGHT * WHEEL_VISIBLE_ITEMS),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = WHEEL_ITEM_HEIGHT * (WHEEL_VISIBLE_ITEMS / 2)),
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(years) { index, year ->
                val distance = abs(index - centeredIndex)
                val scale = (1f - distance * 0.18f).coerceAtLeast(0.55f)
                val alpha = (1f - distance * 0.3f).coerceAtLeast(0.25f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(WHEEL_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = if (distance == 0) FontWeight.Medium else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    )
                }
            }
        }
        Divider(modifier = Modifier.offset(y = -WHEEL_ITEM_HEIGHT / 2))
        Divider(modifier = Modifier.offset(y = WHEEL_ITEM_HEIGHT / 2))
    }
}

@Preview(showBackground = true)
@Composable
fun VpYearPickerEmptyPreview() {
    VpTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpYearPicker(
                value = null,
                onValueChange = {},
                label = "Year Of Birth",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VpYearPickerFilledPreview() {
    VpTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VpYearPicker(
                value = 2000,
                onValueChange = {},
                label = "Year Of Birth",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
