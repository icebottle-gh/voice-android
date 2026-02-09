package com.example.temp.components

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A list item Composable that displays a short message and an optional action button.
 *
 * @param message The short message to display on the left side.
 * @param actionText The text for the action button. If null or empty, the button is not shown.
 * @param onActionClick Lambda to be invoked when the action button is clicked.
 * @param modifier Optional [Modifier] for this Composable.
 * @param useTextButton If true, a TextButton will be used for the action; otherwise, a filled Button.
 */
@Composable
fun ActionableListItem(
    message: String,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    useTextButton: Boolean = false // Option to use TextButton for less emphasis
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Pushes message left, button right
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f) // Message takes available space, ensures button stays on the right
                .padding(end = 8.dp) // Space between message and button if button exists
        )

        // Conditionally display the action button
        if (!actionText.isNullOrBlank() && onActionClick != null) {
            if (useTextButton) {
                TextButton(
                    onClick = onActionClick,
                    // contentPadding can be adjusted if needed, e.g., ButtonDefaults.TextButtonContentPadding
                ) {
                    Text(text = actionText)
                }
            } else {
                Button(
                    onClick = onActionClick,
                    // For a smaller button, you might adjust contentPadding
                    // contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = actionText)
                }
            }
        }
    }
}

// Previews
@Preview(showBackground = true, name = "Item with Action Button")
@Composable
fun ActionableListItemWithButtonPreview() {
    MaterialTheme {
        Surface {
            ActionableListItem(
                message = "Your subscription is about to expire.",
                actionText = "Renew",
                onActionClick = { println("Renew clicked") }
            )
        }
    }
}

@Preview(showBackground = true, name = "Item with TextButton Action")
@Composable
fun ActionableListItemWithTextButtonPreview() {
    MaterialTheme {
        Surface {
            ActionableListItem(
                message = "New login from an unrecognized device.",
                actionText = "Review",
                onActionClick = { println("Review clicked") },
                useTextButton = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Item without Action Button")
@Composable
fun ActionableListItemWithoutButtonPreview() {
    MaterialTheme {
        Surface {
            ActionableListItem(
                message = "Update downloaded successfully.",
                actionText = null, // No action text means no button
                onActionClick = null
            )
        }
    }
}

@Preview(showBackground = true, name = "Item with Long Message")
@Composable
fun ActionableListItemWithLongMessagePreview() {
    MaterialTheme {
        Surface {
            ActionableListItem(
                message = "This is a particularly long message that should wrap nicely and still allow the button to be on the right.",
                actionText = "Details",
                onActionClick = { println("Details clicked") }
            )
        }
    }
}
