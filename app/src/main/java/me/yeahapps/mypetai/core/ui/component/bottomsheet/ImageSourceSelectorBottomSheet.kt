package me.yeahapps.mypetai.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIButtonDefaults
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAISecondaryButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceSelectorBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onCameraSourceClick: () -> Unit,
    onGallerySourceClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { PetAIDragHandle() },
        containerColor = Color(0xFF221F03),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.image_source_selector_title),
                    style = PetAITheme.typography.buttonTextDefault,
                    color = PetAITheme.colors.buttonTextSecondary,
                    modifier = Modifier.weight(1f)
                )
                PetAIIconButton(
                    icon = R.drawable.ic_close,
                    colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                    onClick = onDismissRequest
                )
            }
            Spacer(Modifier.size(24.dp))
            PetAISecondaryButton(
                text = stringResource(R.string.image_source_gallery), colors = PetAIButtonDefaults.colors(
                    containerColor = PetAITheme.colors.buttonSecondaryDefault,
                    contentColor = PetAITheme.colors.buttonTextSecondary,
                ), startContent = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_gallery), contentDescription = null)
                }, onClick = onGallerySourceClick, modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.size(16.dp))
            PetAISecondaryButton(
                text = stringResource(R.string.image_source_camera), startContent = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_camera), contentDescription = null)
                }, colors = PetAIButtonDefaults.colors(
                    containerColor = PetAITheme.colors.buttonSecondaryDefault,
                    contentColor = PetAITheme.colors.buttonTextSecondary,
                ), onClick = onCameraSourceClick, modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.size(32.dp))
        }
    }
}