package me.yeahapps.mypetai.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import me.yeahapps.mypetai.core.ui.navigation.PetAINavHost
import me.yeahapps.mypetai.feature.discover.ui.screen.DiscoverScreen
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingScreen
import me.yeahapps.mypetai.feature.root.ui.screen.RootScreen

@Composable
fun PetAIApp(navController: NavHostController) {
    PetAINavHost(navController = navController, startDestination = OnboardingScreen)
}