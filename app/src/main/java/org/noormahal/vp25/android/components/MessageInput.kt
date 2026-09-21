package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.OrangeA200
import org.noormahal.vp25.android.theme.OrangeBlack800
import org.noormahal.vp25.android.theme.VpTheme

enum class MessageInputState { EMPTY, TYPING, TYPED, RECORDING_HOLD, RECORDING_LOCKED }

private val INPUT_PILL_SHAPE = RoundedCornerShape(percent = 50)
private val RECORD_BUTTON_SIZE = 44.dp
private val DRAG_LOCK_THRESHOLD = 40.dp

@Composable
fun MessageInput(
    state: MessageInputState,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    recordingDuration: String = "00:00",
    onSendClick: () -> Unit = {},
    onMicPress: () -> Unit = {},
    onMicRelease: () -> Unit = {},
    onDragToLock: () -> Unit = {},
    onDeleteRecording: () -> Unit = {}
) {
    val isRecording = state == MessageInputState.RECORDING_HOLD || state == MessageInputState.RECORDING_LOCKED

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant, INPUT_PILL_SHAPE)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (isRecording) R.drawable.baseline_mic_24 else R.drawable.baseline_insert_emoticon_24
                ),
                contentDescription = null,
                tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))

            if (isRecording) {
                Text(
                    text = recordingDuration,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (state == MessageInputState.RECORDING_LOCKED) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.outline_delete_24),
                        contentDescription = "Delete recording",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable(onClick = onDeleteRecording)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Message",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Box(contentAlignment = Alignment.BottomCenter) {
            if (state == MessageInputState.RECORDING_HOLD) {
                // The lock circle is the drag target; the double-chevron below hints
                // at the direction. Both disappear once RECORDING_LOCKED takes over.
                Column(
                    modifier = Modifier.offset(y = (-84).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        tonalElevation = 2.dp,
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_lock_24),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.outline_keyboard_double_arrow_up_24),
                        contentDescription = "Slide up to lock recording",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            RecordSendButton(
                state = state,
                onSendClick = onSendClick,
                onMicPress = onMicPress,
                onMicRelease = onMicRelease,
                onDragToLock = onDragToLock
            )
        }
    }
}

@Composable
private fun RecordSendButton(
    state: MessageInputState,
    onSendClick: () -> Unit,
    onMicPress: () -> Unit,
    onMicRelease: () -> Unit,
    onDragToLock: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showSendIcon = state == MessageInputState.TYPING ||
        state == MessageInputState.TYPED ||
        state == MessageInputState.RECORDING_LOCKED
    val isHoldToRecord = state == MessageInputState.EMPTY || state == MessageInputState.RECORDING_HOLD

    val icon = if (showSendIcon) R.drawable.baseline_send_24 else R.drawable.baseline_mic_24

    val gestureModifier = if (isHoldToRecord) {
        Modifier.pointerInput(state) {
            val lockThresholdPx = DRAG_LOCK_THRESHOLD.toPx()
            awaitEachGesture {
                awaitFirstDown()
                onMicPress()
                var locked = false
                var accumulatedDragY = 0f
                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull() ?: break
                    if (!change.pressed) {
                        if (!locked) onMicRelease()
                        break
                    }
                    accumulatedDragY += change.positionChange().y
                    if (!locked && accumulatedDragY < -lockThresholdPx) {
                        locked = true
                        onDragToLock()
                    }
                    change.consume()
                }
            }
        }
    } else {
        Modifier.clickable(onClick = onSendClick)
    }

    Surface(
        modifier = modifier
            .size(RECORD_BUTTON_SIZE)
            .then(gestureModifier),
        shape = CircleShape,
        color = OrangeA200,
        contentColor = OrangeBlack800,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = if (showSendIcon) "Send" else "Record voice message",
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 412)
@Preview(name = "Dark", showBackground = true, widthDp = 412, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MessageInputPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MessageInput(state = MessageInputState.EMPTY, value = "", onValueChange = {})
                var typingValue by remember { mutableStateOf("Message") }
                MessageInput(state = MessageInputState.TYPING, value = typingValue, onValueChange = { typingValue = it })
                MessageInput(state = MessageInputState.TYPED, value = "Message", onValueChange = {})
                Spacer(modifier = Modifier.height(80.dp))
                MessageInput(state = MessageInputState.RECORDING_HOLD, value = "", onValueChange = {}, recordingDuration = "00:00")
                MessageInput(state = MessageInputState.RECORDING_LOCKED, value = "", onValueChange = {}, recordingDuration = "00:87")
            }
        }
    }
}
