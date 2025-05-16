package dev.ymuratov.petai.feature.profile.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
object ProfileScreen

@Composable
fun ProfileContainer(modifier: Modifier = Modifier) {
    ProfileContent(modifier = modifier)
}

@Composable
private fun ProfileContent(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Text(text = "profile")
        }
    }
}