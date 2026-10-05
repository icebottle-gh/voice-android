package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.noormahal.vp25.android.theme.VpTheme

private val DIALOG_CORNER_RADIUS = 24.dp
private val DIALOG_CONTENT_PADDING = 24.dp
private val DIALOG_GAP = 12.dp

data class VpDialogButtonSpec(
    val text: String,
    val onClick: () -> Unit,
    val style: ButtonStyle = ButtonStyle.ROUND_PRIMARY_OUTLINE
)

@Composable
fun VpDialog(
    onDismissRequest: () -> Unit,
    buttons: List<VpDialogButtonSpec>,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        VpDialogCard(
            buttons = buttons,
            modifier = modifier,
            title = title,
            description = description,
            content = content
        )
    }
}


@Composable
internal fun VpDialogCard(
    buttons: List<VpDialogButtonSpec>,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(DIALOG_CORNER_RADIUS),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(DIALOG_CONTENT_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DIALOG_GAP)
        ) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            content?.invoke()

            if (buttons.size <= 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DIALOG_GAP)
                ) {
                    buttons.forEach { button ->
                        VpButton(
                            label = { Text(button.text) },
                            onClick = button.onClick,
                            style = button.style,
                            fullWidth = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(DIALOG_GAP)
                ) {
                    buttons.forEach { button ->
                        VpButton(
                            label = { Text(button.text) },
                            onClick = button.onClick,
                            style = button.style,
                            fullWidth = true
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpDialogTwoButtonPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VpDialogCard(
                title = "Title",
                description = "Description. Lorem ipsum dolor sit amet consectetur adipiscing elit, sed do.",
                buttons = listOf(
                    VpDialogButtonSpec(text = "Button 1", onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE),
                    VpDialogButtonSpec(text = "Button 2", onClick = {}, style = ButtonStyle.ROUND_PRIMARY)
                )
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpDialogContentSlotPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VpDialogCard(
                title = "Edit Nickname",
                buttons = listOf(
                    VpDialogButtonSpec(text = "Cancel", onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE),
                    VpDialogButtonSpec(text = "Save", onClick = {}, style = ButtonStyle.ROUND_PRIMARY)
                ),
                content = {
                    VpTextField(
                        value = "Buddy",
                        onValueChange = {},
                        placeholder = "Enter nickname or leave blank",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpDialogThreeButtonPreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VpDialogCard(
                title = "Title",
                description = "Description. Lorem ipsum dolor sit amet consectetur adipiscing elit, sed do.",
                buttons = listOf(
                    VpDialogButtonSpec(text = "Button", onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE),
                    VpDialogButtonSpec(text = "Button", onClick = {}, style = ButtonStyle.ROUND_PRIMARY_OUTLINE),
                    VpDialogButtonSpec(text = "Button", onClick = {}, style = ButtonStyle.ROUND_PRIMARY)
                )
            )
        }
    }
}
