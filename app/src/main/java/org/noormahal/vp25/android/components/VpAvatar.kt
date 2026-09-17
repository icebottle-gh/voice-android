package org.noormahal.vp25.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

@Composable
fun VpAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 84.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Avatar",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

@Preview
@Composable
fun VpAvatarLargePreview() {
    VpTheme{
        VpAvatar()
    }

}

@Preview
@Composable
fun VpAvatarSmallPreview() {
    VpTheme{
        VpAvatar(size = 40.dp)
    }
}
