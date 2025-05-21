package me.yeahapps.mypetai.feature.create.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
object CreateScreen

@Composable
fun CreateContainer(modifier: Modifier = Modifier) {
    CreateContent(modifier = modifier)
}

@Composable
private fun CreateContent(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Text(text = "create")
        }
    }
}