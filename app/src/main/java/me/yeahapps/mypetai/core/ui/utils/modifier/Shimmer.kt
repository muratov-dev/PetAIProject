package me.yeahapps.mypetai.core.ui.utils.modifier

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

const val SHIMMER_WAVE_ANGLE_DEFAULT = 15f

fun Modifier.shimmerOnContentLoadingAnimation(
    color: Color = Color.White,
    backgroundColor: Color = Color.Transparent,
    desiredWidthOfShadowBrush: Int = 0,
    durationMillis: Int = 2500,
    cornerRadius: Dp = 16.dp,
    angle: Float = SHIMMER_WAVE_ANGLE_DEFAULT,
): Modifier {
    return composed {
        val shimmerColors = listOf(
            color.copy(alpha = 0.2f),
            color.copy(alpha = 0.4f),
            color.copy(alpha = 0.6f),
            color.copy(alpha = 0.8f),
            color.copy(alpha = 0.6f),
            color.copy(alpha = 0.4f),
            color.copy(alpha = 0.2f),
        )
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }

        val viewDiagonalLength = sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2.0))

        val widthOfShadowBrush = if (desiredWidthOfShadowBrush == 0) {
            viewDiagonalLength.toFloat() * 0.8f
        } else desiredWidthOfShadowBrush.toFloat()

        val transition = rememberInfiniteTransition(label = "")

        val translateAnimation = transition.animateFloat(
            initialValue = -widthOfShadowBrush,
            targetValue = viewDiagonalLength.toFloat() + widthOfShadowBrush,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "animation",
        )

        val angleInRadians = if (angle in 0f..90f) {
            Math.toRadians(angle.toDouble()).toFloat()
        } else Math.toRadians(SHIMMER_WAVE_ANGLE_DEFAULT.toDouble()).toFloat()

        this
            .drawWithContent {
                drawContent()

                val startX = translateAnimation.value * cos(angleInRadians) - widthOfShadowBrush
                val startY = translateAnimation.value * sin(angleInRadians)
                val endX = startX + widthOfShadowBrush * cos(angleInRadians)
                val endY = startY + widthOfShadowBrush * sin(angleInRadians)

                drawRoundRect(
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                    brush = SolidColor(backgroundColor),
                )
                drawRoundRect(
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(x = startX, y = startY),
                        end = Offset(x = endX, y = endY),
                        tileMode = TileMode.Clamp,
                    ),
                )
            }
            .onGloballyPositioned { size = it.size }
    }
}