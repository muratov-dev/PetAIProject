package me.yeahapps.mypetai.feature.profile.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
object MyWorkInfoScreen

@Composable
fun MyWorkInfoContainer(modifier: Modifier = Modifier) {
    MyWorkInfoContent(modifier = modifier)
}

@Composable
private fun MyWorkInfoContent(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}