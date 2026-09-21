package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.theme.VpSpacing
import org.noormahal.vp25.android.theme.VpTheme

enum class ListTitleRightControl { ICON, BUTTON_TERTIARY, NONE }

@Composable
fun VpListTitle(
    title: String,
    modifier: Modifier = Modifier,
    rightControl: ListTitleRightControl = ListTitleRightControl.NONE,
    @DrawableRes rightIcon: Int = R.drawable.baseline_arrow_forward_24,
    buttonLabel: String = "Edit",
    onRightControlClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (rightControl == ListTitleRightControl.ICON) {
                    Modifier.clickable(onClick = onRightControlClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = VpSpacing.screenHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        when (rightControl) {
            ListTitleRightControl.ICON -> {
                Icon(
                    painter = painterResource(id = rightIcon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            ListTitleRightControl.BUTTON_TERTIARY -> {
                VpButton(
                    label = { Text(buttonLabel) },
                    onClick = onRightControlClick,
                    style = ButtonStyle.PRIMARY_FRAMELESS
                )
            }
            ListTitleRightControl.NONE -> Unit
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VpListTitlePreview() {
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                VpListTitle(title = "Title", rightControl = ListTitleRightControl.ICON)
                VpListTitle(title = "Title", rightControl = ListTitleRightControl.BUTTON_TERTIARY)
                VpListTitle(title = "Title", rightControl = ListTitleRightControl.NONE)
            }
        }
    }
}
