package com.example.temp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PersonListItem(props: PersonListItemProps, onFollowClick: () -> Unit = {}) {
    val displayName = if (props.nickName != null) "[ ${props.nickName} ]" else props.fullName
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp), // Overall padding for the item
        verticalAlignment = Alignment.CenterVertically, // Align items vertically in the center of the row
        horizontalArrangement = Arrangement.SpaceBetween // Pushes text to left and button to right
    ) {
        // Column for Full Name and Subtext
        Column(
            modifier = Modifier
                .weight(1f) // Text column takes available space, pushing button to the end
                .padding(end = 8.dp) // Add some space between text and button
        ) {
            Text(
                text = displayName ?: "No name provided!",
                style = MaterialTheme.typography.titleMedium, // Or titleSmall, bodyLarge
                fontWeight = FontWeight.Bold // Make full name stand out
            )
            Spacer(modifier = Modifier.height(2.dp)) // Small space between fullName and subtext
            if (props.isFollower)
                Text(
                    text = "Follows you",
                    style = MaterialTheme.typography.bodySmall, // Smaller font style for subtext
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Slightly muted color
                    // You can also use explicit font size:
                    // fontSize = 12.sp,
                )
        }

        val a = run { 20 + 10 }


        Button(
            onClick = onFollowClick,
            // Optional: Adjust button padding if needed, but default usually works well
            // contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            val label = if (props.isFollowing) "Unfollow" else "Follow"
            Text(text = label)
        }
    }
}


// Preview for the Composable
@Preview(showBackground = true)
@Composable
fun PersonListItemUnfollowPreview() {
    val props = PersonListItemProps(
        id = "1",
        fullName = "John Doe",
        nickName = "Johnny",
        isFollowing = true,
        isFollower = true
    )
    MaterialTheme { // Wrap in MaterialTheme for proper styling in preview
        Surface {
            PersonListItem(
                props = props
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonListItemFollowPreview() {
    val props = PersonListItemProps(
        id = "1",
        fullName = "John Doe",
        nickName = null,
        isFollowing = false,
        isFollower = false
    )
    MaterialTheme { // Wrap in MaterialTheme for proper styling in preview
        Surface {
            PersonListItem(
                props = props
            )
        }
    }
}



data class PersonListItemProps(
    val id: String,
    val fullName: String?,
    val nickName: String?,
    val isFollowing: Boolean,
    val isFollower: Boolean
)