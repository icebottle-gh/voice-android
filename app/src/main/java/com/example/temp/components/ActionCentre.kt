package com.example.temp.components // Or your preferred UI package

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Import your ActionableListItem from its actual location
import com.example.temp.components.ActionableListItem // Adjust if your file/package is different

// Data class defined within this file specifically for ActionCentre's needs
public data class ActionCentreItem(
    val id: String,
    val displayMessage: String,
    val buttonActionText: String? = null, // Text for the button, if any
    val timestamp: Long = System.currentTimeMillis() // Example additional field
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCentreScreen(
    // These lists will now be of the locally defined ActionCentreItem
    activeDisplayItems: List<ActionCentreItem>,
    historyDisplayItems: List<ActionCentreItem>,
    // Callback when an action button on an item is clicked
    onItemActionClicked: (itemId: String, actionText: String) -> Unit,
    modifier: Modifier = Modifier,
    showActive: Boolean = true
) {
    val tabs = listOf("Active", "History")
    var selectedTabIndex = if (showActive) 0 else 1

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> ItemsListDisplay(
                items = activeDisplayItems,
                onItemAction = onItemActionClicked,
                listType = "Active"
            )
            1 -> ItemsListDisplay(
                items = historyDisplayItems,
                onItemAction = onItemActionClicked,
                listType = "History"
            )
        }
    }
}

@Composable
private fun ItemsListDisplay(
    items: List<ActionCentreItem>,
    onItemAction: (itemId: String, actionText: String) -> Unit,
    listType: String,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No $listType items to display.")
        }
        return
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items, key = { it.id }) { item -> // item is of type ActionCentreItem
            ActionableListItem( // Calling the imported component
                message = item.displayMessage,
                actionText = item.buttonActionText,
                onActionClick = item.buttonActionText?.let { // Create lambda only if buttonActionText exists
                    { onItemAction(item.id, it) }
                },
                modifier = Modifier.fillMaxWidth()
                // Pass any other parameters your ActionableListItem might expect
            )
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionCentreScreenPreview(modifier: Modifier = Modifier, showActive: Boolean = true) {
    val sampleActiveItems = listOf(
        ActionCentreItem(id = "a1", displayMessage = "Active Task 1: Needs review.", buttonActionText = "Review"),
        ActionCentreItem(id = "a2", displayMessage = "Active Task 2: Reminder for tomorrow.", buttonActionText = "Snooze"),
        ActionCentreItem(id = "a3", displayMessage = "Active Info: System update available.")
    )
    val sampleHistoryItems = listOf(
        ActionCentreItem(id = "h1", displayMessage = "History: Task X approved on Jan 10."),
        ActionCentreItem(id = "h2", displayMessage = "History: Alert Y was dismissed.", buttonActionText = "View Details")
    )

        ActionCentreScreen(
            activeDisplayItems = sampleActiveItems,
            historyDisplayItems = sampleHistoryItems,
            onItemActionClicked = { itemId, action ->
                println("Action '$action' clicked for item '$itemId'")
            },
            modifier = modifier,
            showActive = showActive
        )
}

// === Placeholder for illustration if ActionableListItem was in this file ===
// If ActionableListItem was defined here AND it didn't use a data class:
//
// @Composable
// fun ActionableListItemPlaceholder( // This is NOT the one being imported
//    message: String,
//    actionText: String?,
//    onActionClick: (() -> Unit)?,
//    modifier: Modifier = Modifier
// ) {
//    Row(
//        modifier = modifier.fillMaxWidth().padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(message, modifier = Modifier.weight(1f))
//        if (actionText != null && onActionClick != null) {
//            Button(onClick = onActionClick) { Text(actionText) }
//        }
//    }
// }
// === End of Placeholder ===

