package me.yeahapps.mypetai.feature.discover.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun TryNowButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 40.dp),
        border = BorderStroke(2.dp, PetAITheme.colors.buttonPrimaryDefault),
        modifier = Modifier.clip(RoundedCornerShape(100.dp)),
        colors = ButtonColors(
            containerColor = PetAITheme.colors.buttonPrimaryDefault.copy(alpha = 0.1f),
            contentColor = PetAITheme.colors.buttonTextPrimary,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
    ) {
        Text(
            text = "Try Now",
            color = PetAITheme.colors.buttonTextSecondary,
            style = PetAITheme.typography.buttonTextDefault
        )
    }
}