package dev.ymuratov.petai.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dev.ymuratov.petai.core.ui.navigation.PetAINavHost
import dev.ymuratov.petai.feature.discover.ui.screen.DiscoverScreen

@Composable
fun PetAIApp(navController: NavHostController) {
    PetAINavHost(navController = navController, startDestination = DiscoverScreen)
}