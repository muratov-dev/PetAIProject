package dev.ymuratov.petai.core.ui.component.topbar

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PetAITopBarTitleText(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
    )
}