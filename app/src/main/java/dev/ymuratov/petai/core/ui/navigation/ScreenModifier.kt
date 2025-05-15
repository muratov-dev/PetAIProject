package dev.ymuratov.petai.core.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ymuratov.petai.core.ui.theme.PetAITheme

@Composable
fun Modifier.commonModifier(): Modifier {
    return fillMaxSize().background(color = PetAITheme.colors.backgroundPrimary).systemBarsPadding()
}