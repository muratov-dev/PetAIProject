package me.yeahapps.mypetai.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.modifier.shimmerOnContentLoadingAnimation
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel
import kotlin.random.Random

@Composable
fun SongCard(
    song: MyWorkModel, modifier: Modifier = Modifier, onSongClick: (MyWorkModel) -> Unit = {}
) {
    val listensCount = Random.nextInt(10, 900)
    SongCardContent(
        SongContentModel(song.imageUrl, song.title, listensCount),
        modifier = modifier,
        onSongClick = { onSongClick(song) })
}

@Composable
fun SongCard(
    song: SongModel, modifier: Modifier = Modifier, onSongClick: (SongModel) -> Unit = {}
) {
    val listensCount = Random.nextInt(10, 900)
    SongCardContent(
        SongContentModel(song.video.imageUrl, song.name, listensCount),
        modifier = modifier,
        onSongClick = { onSongClick(song) })
}

@Composable
private fun SongCardContent(
    song: SongContentModel, modifier: Modifier = Modifier, onSongClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(width = 168.dp, height = 208.dp)
            .clickable(onClick = { onSongClick() })
            .clip(RoundedCornerShape(32.dp))
    ) {
        PetAIAsyncImage(modifier = Modifier.matchParentSize(), data = song.imageUrl)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter), onDraw = {
                drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC040401))))
            })
        if (song.listensCount > 0) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
                    .background(
                        color = PetAITheme.colors.backgroundPrimary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_music),
                    contentDescription = null,
                    tint = PetAITheme.colors.textPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(1.dp))
                Text(
                    text = "${song.listensCount}k",
                    style = PetAITheme.typography.labelMedium,
                    color = PetAITheme.colors.textPrimary
                )
            }
        }

        Text(
            text = song.name,
            style = PetAITheme.typography.textMedium,
            color = PetAITheme.colors.textPrimary,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
fun SongCardPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 168.dp, height = 208.dp)
            .clip(RoundedCornerShape(32.dp))
            .shimmerOnContentLoadingAnimation()
    )
}


data class SongContentModel(
    val imageUrl: String, val name: String, val listensCount: Int = 0
)