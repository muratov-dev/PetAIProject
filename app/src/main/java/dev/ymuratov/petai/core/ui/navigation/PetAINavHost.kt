package dev.ymuratov.petai.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.ymuratov.petai.feature.profile.ui.screen.MyWorksContainer
import dev.ymuratov.petai.feature.profile.ui.screen.MyWorksScreen
import dev.ymuratov.petai.feature.root.ui.screen.RootContainer
import dev.ymuratov.petai.feature.root.ui.screen.RootScreen

@Composable
fun PetAINavHost(startDestination: Any, navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable<RootScreen> {
            RootContainer(modifier = Modifier.commonModifier(), parentNavController = navController)
        }
        composable<MyWorksScreen> {
            MyWorksContainer(modifier = Modifier.commonModifier())
        }
    }
}