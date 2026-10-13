package org.noormahal.vp25.android.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.noormahal.vp25.android.components.VpBottomBar
import org.noormahal.vp25.android.components.VpTopAppBar
import org.noormahal.vp25.android.components.VpTopAppBarAction
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.navigation.screensWithBottom


@Composable
fun HomeShellScaffold(
    currentRoute: String?,
    title: String,
    titleColor: Color = Color.Unspecified,
    titleFontWeight: FontWeight? = null,
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    showBottomBar: Boolean = true,
    onBottomScreenClick: (Screen) -> Unit = {},
    actions: List<VpTopAppBarAction> = emptyList(),
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                VpBottomBar(
                    currentRoute = currentRoute,
                    items = screensWithBottom,
                    onBottomScreenClick = onBottomScreenClick
                )
            }
        },
        topBar = {
            VpTopAppBar(
                title = title,
                titleColor = titleColor,
                titleFontWeight = titleFontWeight,
                showLeadingIcon = showBackButton,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                leadingIconContentDescription = "Back",
                onLeadingIconClick = onBack,
                actions = actions
            )
        },
        floatingActionButton = floatingActionButton,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { pd -> content(pd) }
}
