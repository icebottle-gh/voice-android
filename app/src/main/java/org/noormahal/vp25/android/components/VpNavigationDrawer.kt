package org.noormahal.vp25.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    userName: String? = null,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(drawerWidth)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VpAvatar(size = 56.dp)
                    if (!userName.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
            ),
            userName = "John Doe"
        ) {}
    }
}
