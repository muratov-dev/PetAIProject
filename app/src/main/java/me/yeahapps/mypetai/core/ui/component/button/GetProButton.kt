package me.yeahapps.mypetai.core.ui.component.button

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun GetProButton(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    var isButtonExpanded by remember { mutableStateOf(true) }
    LaunchedEffect(isButtonExpanded) {
        if (isButtonExpanded) {
            delay(5000)
            isButtonExpanded = false
        }
    }

    Button(
        onClick = { if (isButtonExpanded) onButtonClick() else isButtonExpanded = true },
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        colors = ButtonColors(
            containerColor = PetAITheme.colors.buttonPrimaryDefault,
            contentColor = PetAITheme.colors.buttonTextPrimary,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        modifier = modifier.wrapContentSize()
    ) {
        Row(
            modifier = Modifier.animateContentSize(tween(300)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_ribbon), contentDescription = null)
            if (isButtonExpanded) {
                Text(
                    text = stringResource(R.string.common_get_pro_button_text),
                    color = PetAITheme.colors.buttonTextPrimary,
                    style = PetAITheme.typography.buttonTextDefault
                )
            }
        }
    }

}