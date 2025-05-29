package me.yeahapps.mypetai.feature.create.ui.component.create

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun DashedDivider(
    color: Color = PetAITheme.colors.buttonPrimaryDefault,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 5.dp,
    gapLength: Dp = 5.dp,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val density = LocalDensity.current
    Canvas(modifier = modifier.height(strokeWidth)) {
        val y = size.height / 2
        val dashPx = with(density) { dashLength.toPx() }
        val gapPx = with(density) { gapLength.toPx() }

        var startX = 0f
        while (startX < size.width) {
            val endX = (startX + dashPx).coerceAtMost(size.width)
            drawLine(
                color = color,
                start = Offset(x = startX, y = y),
                end = Offset(x = endX, y = y),
                strokeWidth = strokeWidth.toPx()
            )
            startX += dashPx + gapPx
        }
    }
}