package com.example.temp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Still useful for MaterialTheme, Text, Button styles
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Actionable(
    modifier: Modifier = Modifier, // Modifier for the root Column
    title: String? = null,
    message: String,
    primaryActionText: String,
    onPrimaryActionClick: () -> Unit,
    secondaryActionText: String,
    onSecondaryActionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp) // Standard padding for a page section
    ) {
        // Optional Title
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall, // Adjusted for page context
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp), // Space after title
                textAlign = TextAlign.Start // Titles in page flow are usually start-aligned
            )
        }

        // Message Area
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge, // Standard body text for a page
            // fontSize and lineHeight can be inherited from theme or set explicitly if needed
            // fontSize = 16.sp,
            // lineHeight = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp) // More space after a potentially long message block
        )

        // Actions at the end
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            // .padding(top = 16.dp) // Padding is handled by message's bottom padding now
            horizontalArrangement = Arrangement.End, // Keep actions to the right
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onSecondaryActionClick,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(secondaryActionText)
            }
            Button(
                onClick = onPrimaryActionClick
            ) {
                Text(primaryActionText)
            }
        }
    }
}

@Preview(showBackground = true, name = "Actionable - Page Section Look")
@Composable
fun ActionablePageSectionPreview(modifier: Modifier = Modifier) {
        Actionable(
            title = "Next Steps",
            message = "This is a moderately long message that explains the situation and what needs to be done. " +
                    "Since this component is designed to look like a normal part of the page, it will flow " +
                    "naturally with other content above or below it. The parent container (like this Column) " +
                    "would handle scrolling if the overall page content is too long.",
            primaryActionText = "Proceed",
            onPrimaryActionClick = {},
            secondaryActionText = "Learn More",
            onSecondaryActionClick = {},
            modifier = modifier
        )

}

@Preview(showBackground = true, name = "Actionable - No Title, Page Look")
@Composable
fun ActionableNoTitlePagePreview() {
    MaterialTheme {
        Actionable(
            message = "Your settings have been updated. You can review them or continue to the dashboard.",
            primaryActionText = "Dashboard",
            onPrimaryActionClick = {},
            secondaryActionText = "Review Settings",
            onSecondaryActionClick = {}
        )
    }
}

