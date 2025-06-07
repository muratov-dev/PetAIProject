package me.yeahapps.mypetai.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import me.yeahapps.mypetai.core.ui.navigation.PetAINavHost

@Composable
fun PetAIApp(navController: NavHostController, startDestination: Any, isFirstLaunch: Boolean) {

    PetAINavHost(navController = navController, startDestination = startDestination, isFirstLaunch = isFirstLaunch)
}