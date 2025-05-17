package dev.ymuratov.petai.feature.profile.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import dev.ymuratov.petai.R
import dev.ymuratov.petai.core.ui.theme.PetAITheme
import kotlinx.serialization.Serializable

@Serializable
object ProfileScreen

@Composable
fun ProfileContainer(modifier: Modifier = Modifier) {
    ProfileContent(modifier = modifier)
}

@Composable
private fun ProfileContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "My Profile", color = Color.White, style = PetAITheme.typography.titleBlack)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {},
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = PetAITheme.colors.buttonSecondaryDefault,
                contentColor = PetAITheme.colors.buttonTextSecondary
            ),
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
        ) {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_my_works), contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text(text = "My Works (0)", style = PetAITheme.typography.buttonTextRegular, modifier = Modifier.weight(1f))
            Spacer(Modifier.size(8.dp))
            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right), contentDescription = null)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = PetAITheme.colors.buttonSecondaryDefault,
                    contentColor = PetAITheme.colors.buttonTextSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_share), contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(text = "Share", style = PetAITheme.typography.buttonTextRegular, modifier = Modifier.weight(1f))
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = PetAITheme.colors.buttonSecondaryDefault,
                    contentColor = PetAITheme.colors.buttonTextSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_rate), contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(text = "Rate Us", style = PetAITheme.typography.buttonTextRegular, modifier = Modifier.weight(1f))
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = PetAITheme.colors.buttonSecondaryDefault,
                    contentColor = PetAITheme.colors.buttonTextSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_terms), contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Terms of Use",
                    style = PetAITheme.typography.buttonTextRegular,
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = PetAITheme.colors.buttonSecondaryDefault,
                    contentColor = PetAITheme.colors.buttonTextSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_policy), contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Privacy Policy",
                    style = PetAITheme.typography.buttonTextRegular,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}