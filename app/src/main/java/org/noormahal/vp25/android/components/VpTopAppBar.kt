package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

/** Visible action icons on a bar are capped at this count; matches Material's guidance to overflow beyond ~5. */
private const val MAX_VISIBLE_ACTIONS = 5

enum class VpTopAppBarType { Default, Actions }

data class VpTopAppBarAction(
    val icon: ImageVector,
    val label: String? = null,
    val contentDescription: String? = label,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VpTopAppBar(
    title: String,
    showTitle: Boolean = true,
    showLeadingIcon: Boolean = true,
    leadingIcon: ImageVector = Icons.Filled.ArrowBack,
    leadingIconContentDescription: String? = null,
    onLeadingIconClick: () -> Unit = {},
    showTrailingIcon: Boolean = true,
    type: VpTopAppBarType = VpTopAppBarType.Default,
    actions: List<VpTopAppBarAction> = emptyList(),
) {
    var showOverflowMenu by remember { mutableStateOf(false) }
    val visibleActions = actions.take(MAX_VISIBLE_ACTIONS)

    TopAppBar(
        windowInsets = WindowInsets.statusBarsIgnoringVisibility,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            if (showTitle) {
                Text(
                    text = title,
                    modifier = Modifier.heightIn(max = 30.dp)
                )
            }
        },
        navigationIcon = {
            if (showLeadingIcon) {
                IconButton(onClick = onLeadingIconClick) {
                    Icon(imageVector = leadingIcon, contentDescription = leadingIconContentDescription)
                }
            }
        },
        actions = {
            if (showTrailingIcon) {
                when (type) {
                    VpTopAppBarType.Actions -> {
                        visibleActions.forEach { action ->
                            IconButton(onClick = action.onClick) {
                                Icon(imageVector = action.icon, contentDescription = action.contentDescription)
                            }
                        }
                    }

                    VpTopAppBarType.Default -> {
                        IconButton(onClick = { showOverflowMenu = true }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }

                        MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(8.dp))) {
                            DropdownMenu(
                                expanded = showOverflowMenu,
                                onDismissRequest = { showOverflowMenu = false }
                            ) {
                                visibleActions.forEach { action ->
                                    DropdownMenuItem(
                                        text = { Text(text = action.label ?: "") },
                                        onClick = {
                                            showOverflowMenu = false
                                            action.onClick()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun VpTopAppBarPreview() {
    VpTheme{
        VpTopAppBar(
            title = "Voice",
            actions = listOf(
                VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = {}),
                VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = {}),
                VpTopAppBarAction(icon = Icons.Default.MoreVert, label = "Drop down item", onClick = {}),
            )
        )
    }

}

@Preview
@Composable
fun VpTopAppBarActionsTypePreview() {
    VpTheme{
        VpTopAppBar(
            title = "Voice",
            type = VpTopAppBarType.Actions,
            actions = listOf(
                VpTopAppBarAction(icon = Icons.Default.MoreVert, contentDescription = "Search", onClick = {}),
                VpTopAppBarAction(icon = Icons.Default.MoreVert, contentDescription = "Filter", onClick = {}),
            )
        )
    }

}
