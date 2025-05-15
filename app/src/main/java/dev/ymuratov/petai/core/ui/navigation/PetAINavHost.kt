package dev.ymuratov.petai.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.ymuratov.petai.feature.discover.ui.screen.DiscoverContainer
import dev.ymuratov.petai.feature.discover.ui.screen.DiscoverScreen

@Composable
fun PetAINavHost(startDestination: Any, navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable<DiscoverScreen> {
            DiscoverContainer(modifier = Modifier.commonModifier())
        }
    }
}