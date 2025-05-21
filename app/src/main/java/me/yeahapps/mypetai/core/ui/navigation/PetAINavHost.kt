package me.yeahapps.mypetai.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.yeahapps.mypetai.feature.discover.domain.model.DiscoverNavType
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.ui.screen.SongInfoContainer
import me.yeahapps.mypetai.feature.discover.ui.screen.SongInfoScreen
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingContainer
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingScreen
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingSubscriptionContainer
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingSubscriptionScreen
import me.yeahapps.mypetai.feature.profile.ui.screen.MyWorksContainer
import me.yeahapps.mypetai.feature.profile.ui.screen.MyWorksScreen
import me.yeahapps.mypetai.feature.root.ui.screen.RootContainer
import me.yeahapps.mypetai.feature.root.ui.screen.RootScreen
import kotlin.reflect.typeOf

@Composable
fun PetAINavHost(startDestination: Any, navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable<OnboardingScreen> {
            OnboardingContainer(modifier = Modifier.commonModifier(), navigateToSubsOnboarding = {
                navController.navigate(OnboardingSubscriptionScreen)
            })
        }
        composable<OnboardingSubscriptionScreen> {
            OnboardingSubscriptionContainer(modifier = Modifier.commonModifier())
        }
        composable<RootScreen> {
            RootContainer(modifier = Modifier.commonModifier(), parentNavController = navController)
        }
        composable<SongInfoScreen>(typeMap = mapOf(typeOf<SongModel>() to DiscoverNavType.SongType)) {
            SongInfoContainer(modifier = Modifier.commonModifier(), navigateUp = {
                navController.navigateUp()
            })
        }
        composable<MyWorksScreen> {
            MyWorksContainer(modifier = Modifier.commonModifier())
        }
    }
}