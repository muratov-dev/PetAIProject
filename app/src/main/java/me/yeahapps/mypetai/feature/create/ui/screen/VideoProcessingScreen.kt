package me.yeahapps.mypetai.feature.create.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.feature.create.ui.state.VideoProcessingState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.VideoProcessingViewModel
import java.io.File

@Serializable
data class VideoProcessingScreen(
    val imageUri: String, val audioUri: String
)

@Composable
fun VideoProcessingScreenContainer(
    modifier: Modifier = Modifier, viewModel: VideoProcessingViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    VideoProcessingScreenContent(modifier = modifier, state = state)
}

@Composable
private fun VideoProcessingScreenContent(
    modifier: Modifier = Modifier, state: VideoProcessingState = VideoProcessingState()
) {
    val context = LocalContext.current

    Scaffold(modifier = modifier) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = state.videoPath.toString())
            Button(onClick = {
                val file = state.videoPath?.let { File(state.videoPath) }
                if (file != null && file.exists()) {
                    shareFile(context, file, "video/mp4")
                }
            }) { }
        }
    }
}

fun shareFile(context: Context, file: File, mimeType: String) {
    val uri = FileProvider.getUriForFile(
        context, "${context.packageName}.provider", file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(shareIntent, "Поделиться через:")
    )
}