package me.yeahapps.mypetai.app

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingScreen
import me.yeahapps.mypetai.feature.root.ui.screen.RootScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            val navController = rememberNavController()
            PetAITheme {
                PetAIApp(
                    navController,
                    startDestination = if (sharedPreferences.getBoolean("isFirstLaunch", true)) OnboardingScreen
                    else RootScreen
                )
            }
        }
    }
}