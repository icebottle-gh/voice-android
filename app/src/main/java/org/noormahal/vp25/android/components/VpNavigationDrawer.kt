package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.noormahal.vp25.android.theme.VpTheme

data class VpDrawerItem(
    val label: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit
)

@Composable
fun VpNavigationDrawer(
    drawerState: DrawerState,
    items: List<VpDrawerItem>,
    modifier: Modifier = Modifier,
    drawerWidth: Dp = 240.dp,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(drawerWidth)) {
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        icon = item.icon?.let { icon -> { Icon(icon, contentDescription = null) } },
                        selected = false,
                        shape = RectangleShape,
                        onClick = {
                            scope.launch { drawerState.close() }
                            item.onClick()
                        }
                    )
                }
            }
        },
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun VpNavigationDrawerPreview() {
    VpTheme {
        VpNavigationDrawer(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            items = listOf(
                VpDrawerItem(label = "Logout", onClick = {})
            )
        ) {}
    }
}
