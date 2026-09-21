package org.noormahal.vp25.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun VpTabRow(
    titles: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        titles.forEachIndexed { index, title ->
            VpTab(
                title = title,
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun VpTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = if (selected) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val indicatorColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val indicatorHeight = if (selected) 2.dp else 1.dp

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(indicatorHeight)
                .background(indicatorColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VpTabRowPreview() {
    VpTheme {
        VpTabRow(
            titles = listOf("Title", "Title"),
            selectedIndex = 0,
            onTabSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VpTabRowManyTabsPreview() {
    VpTheme {
        VpTabRow(
            titles = listOf("Chats", "Groups", "Calls", "Status"),
            selectedIndex = 2,
            onTabSelected = {}
        )
    }
}
