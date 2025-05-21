package me.yeahapps.mypetai.feature.profile.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAISecondaryButton
import me.yeahapps.mypetai.core.ui.component.topbar.PetAISecondaryTopAppBar
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R

@Serializable
object ProfileScreen

@Composable
fun ProfileContainer(modifier: Modifier = Modifier, navigateToMyWorks: () -> Unit) {
    ProfileContent(modifier = modifier, navigateToMyWorks = navigateToMyWorks)
}

@Composable
private fun ProfileContent(modifier: Modifier = Modifier, navigateToMyWorks: () -> Unit) {
    Column(modifier = modifier) {
        PetAISecondaryTopAppBar(title = {
            Text(
                text = "My Profile",
                color = Color.White,
                style = PetAITheme.typography.titleBlack,
                modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
            )
        })
        Spacer(modifier = Modifier.height(8.dp))
        PetAISecondaryButton(
            text = "My Works (0)",
            onClick = navigateToMyWorks,
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_my_works), contentDescription = null)
            },
            endContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right), contentDescription = null)
            })
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PetAISecondaryButton(text = "Share", onClick = {}, modifier = Modifier.fillMaxWidth(), startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_share), contentDescription = null)
            })
            PetAISecondaryButton(text = "Rate Us", onClick = {}, modifier = Modifier.fillMaxWidth(), startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_rate), contentDescription = null)
            })
            PetAISecondaryButton(
                text = "Terms of Use",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                startContent = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_terms), contentDescription = null)
                })
            PetAISecondaryButton(
                text = "Privacy Policy",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                startContent = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_policy), contentDescription = null)
                })
        }
    }
}