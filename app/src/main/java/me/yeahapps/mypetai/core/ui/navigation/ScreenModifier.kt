package me.yeahapps.mypetai.core.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun Modifier.commonModifier(): Modifier {
    return fillMaxSize().background(color = PetAITheme.colors.backgroundPrimary)
}