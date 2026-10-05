package org.noormahal.vp25.android.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.noormahal.vp25.android.theme.VpTheme

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    val shimmerColor = MaterialTheme.colorScheme.surfaceVariant
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                shimmerColor.copy(alpha = 0.6f),
                shimmerColor.copy(alpha = 0.2f),
                shimmerColor.copy(alpha = 0.6f)
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}


@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    widthFraction: Float = 1f,
    height: Dp,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    Spacer(
        modifier = modifier
            .let { if (width != null) it.width(width) else it.fillMaxWidth(widthFraction) }
            .height(height)
            .clip(shape)
            .shimmerEffect()
    )
}


@Composable
fun ShimmerLine(
    modifier: Modifier = Modifier,
    widthFraction: Float = 1f,
    height: Dp = 14.dp
) {
    ShimmerBox(
        modifier = modifier,
        widthFraction = widthFraction,
        height = height,
        shape = RoundedCornerShape(4.dp)
    )
}

@Composable
fun ShimmerCircle(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Spacer(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .shimmerEffect()
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShimmerPrimitivesPreview() {
    VpTheme {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ShimmerCircle()
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    ShimmerLine(widthFraction = 0.5f)
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerLine(widthFraction = 0.8f)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            ShimmerBox(height = 120.dp)
        }
    }
}
