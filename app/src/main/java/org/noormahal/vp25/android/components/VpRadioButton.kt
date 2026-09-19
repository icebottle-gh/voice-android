 package org.noormahal.vp25.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun VpRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                id = if (selected) {
                    R.drawable.outline_radio_button_checked_24
                } else {
                    R.drawable.round_radio_button_unchecked_24
                }
            ),
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun VpRadioGroup(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VpRadioButton(
                    selected = index == selectedIndex,
                    onClick = { onOptionSelected(index) }
                )
                Box(modifier = Modifier.width(4.dp))
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VpRadioGroupPreview() {
    VpTheme {
        var selectedIndex by remember { mutableStateOf(1) }
        VpRadioGroup(
            options = listOf("Everyone", "Contacts only", "Nobody"),
            selectedIndex = selectedIndex,
            onOptionSelected = { selectedIndex = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VpRadioButtonPreview() {
    VpTheme {
        Row {
            VpRadioButton(selected = false, onClick = {})
            Box(modifier = Modifier.width(8.dp))
            VpRadioButton(selected = true, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VpRadioButtonInteractivePreview() {
    VpTheme {
        var selectedIndex by remember { mutableStateOf(0) }
        Row {
            VpRadioButton(selected = selectedIndex == 0, onClick = { selectedIndex = 0 })
            Box(modifier = Modifier.width(8.dp))
            VpRadioButton(selected = selectedIndex == 1, onClick = { selectedIndex = 1 })
        }
    }
}
