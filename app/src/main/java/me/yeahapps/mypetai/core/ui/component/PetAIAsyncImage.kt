package me.yeahapps.mypetai.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import me.yeahapps.mypetai.core.ui.utils.modifier.shimmerOnContentLoadingAnimation


@Composable
fun PetAIAsyncImage(
    modifier: Modifier = Modifier,
    data: Any? = null,
    contentDescription: String? = null,
    @DrawableRes placeholder: Int? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(data).crossfade(true).build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
            is AsyncImagePainter.State.Loading -> ImageShimmer(modifier = modifier)
            else -> ImagePlaceholder(placeholder = placeholder)
        }
    }
}

@Composable
private fun ImageShimmer(modifier: Modifier = Modifier) {
    Box(modifier.shimmerOnContentLoadingAnimation())
}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier, placeholder: Int? = null) {
    Box(modifier = modifier.background(color = Color.LightGray)) {
        placeholder?.let {
            Image(
                painter = painterResource(placeholder),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}