package me.yeahapps.mypetai.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun BoxScope.PetAIDefaultShade(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .drawWithCache {
                val gradient = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent, 0.22f to Color(0xA6040400), 1.0f to Color(0xFF040400)
                    ), start = Offset(0f, 0f), end = Offset(0f, size.height)
                )
                onDrawBehind {
                    drawRect(brush = gradient)
                }
            })
}