package me.yeahapps.mypetai.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import me.yeahapps.mypetai.core.ui.navigation.PetAINavHost
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingScreen

@Composable
fun PetAIApp(navController: NavHostController) {
    PetAINavHost(navController = navController, startDestination = OnboardingScreen)
}