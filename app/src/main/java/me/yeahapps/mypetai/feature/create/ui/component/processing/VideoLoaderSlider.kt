package me.yeahapps.mypetai.feature.create.ui.component.processing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoaderSlider(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.Gray,
    trackHeight: Dp = 6.dp,
) {
    var sliderWidth by remember { mutableFloatStateOf(0f) }
    var tempProgress by remember { mutableFloatStateOf(progress) }
    var isDragging by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(targetValue = tempProgress, label = "")
    val trackHeightPx = with(LocalDensity.current) { trackHeight.toPx() }

    Box(modifier = modifier.height(6.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            sliderWidth = size.width

            drawLine(
                color = trackColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = trackHeightPx,
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color(0xFFC3F960),
                start = Offset(0f, size.height / 2),
                end = Offset(size.width * animatedProgress, size.height / 2),
                strokeWidth = trackHeightPx,
                cap = StrokeCap.Round
            )
        }
    }
    LaunchedEffect(progress) {
        if (!isDragging) tempProgress = progress
    }
}