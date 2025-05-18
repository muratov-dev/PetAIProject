package dev.ymuratov.petai.core.ui.component.button

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.ymuratov.petai.core.ui.theme.PetAITheme

@Composable
fun PetAIButtonText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    style: TextStyle = PetAITheme.typography.buttonTextDefault
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        textAlign = textAlign,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun PetAIButtonText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    style: TextStyle = PetAITheme.typography.buttonTextDefault
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        textAlign = textAlign,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}