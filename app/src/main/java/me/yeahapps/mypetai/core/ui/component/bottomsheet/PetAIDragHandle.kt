package me.yeahapps.mypetai.core.ui.component.bottomsheet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PetAIDragHandle() {
    Canvas(
        Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .width(36.dp)
            .height(5.dp)
            .clip(RoundedCornerShape(100.dp))
    ) {
        drawRect(color = Color(0x667F7F7F))
        drawRect(color = Color(0x80C2C2C2), blendMode = BlendMode.Multiply)
    }
}