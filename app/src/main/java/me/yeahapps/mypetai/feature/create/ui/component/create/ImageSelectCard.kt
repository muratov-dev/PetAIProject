package me.yeahapps.mypetai.feature.create.ui.component.create

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun ImageSelectCard(modifier: Modifier = Modifier, context: Context, imageUri: Uri?, onCardClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(164.dp)
            .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onCardClick), contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            val painter = rememberAsyncImagePainter(ImageRequest.Builder(context).data(imageUri).build())
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.common_upload_image_button_text),
                    color = PetAITheme.colors.textPrimary,
                    style = PetAITheme.typography.headlineSemiBold.copy(fontSize = 18.sp, lineHeight = 27.sp),
                )
            }
        }
    }
}