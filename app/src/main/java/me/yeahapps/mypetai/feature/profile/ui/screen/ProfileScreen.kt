package me.yeahapps.mypetai.feature.profile.ui.screen

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAISecondaryButton
import me.yeahapps.mypetai.core.ui.component.topbar.PetAISecondaryTopAppBar
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.feature.profile.ui.state.ProfileState
import me.yeahapps.mypetai.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToMyWorks: () -> Unit,
    navigateToSubscriptions: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileContent(
        modifier = modifier.systemBarsPadding(),
        state = state,
        navigateToMyWorks = navigateToMyWorks,
        navigateToSubscriptions = navigateToSubscriptions
    )
}

//TODO вынеси ресурсы и добавь логику на кнопки
@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    state: ProfileState = ProfileState(),
    navigateToMyWorks: () -> Unit,
    navigateToSubscriptions: () -> Unit
) {
    val context = LocalContext.current
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
        if (!state.hasSubscription) {
            Image(
                painter = painterResource(R.drawable.im_card_pro),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 1f)
                    .clickable {
                        navigateToSubscriptions()
                    }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        PetAISecondaryButton(
            text = "My Works (${state.myWorksCount})",
            onClick = navigateToMyWorks,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_my_works), contentDescription = null)
            },
            endContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right), contentDescription = null)
            })
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PetAISecondaryButton(text = "Share", onClick = {
                val packageName = context.packageName
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "I recommend the application")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Try this application: https://play.google.com/store/apps/details?id=$packageName"
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share with"))
            }, modifier = Modifier.fillMaxWidth(), startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_share), contentDescription = null)
            })
            PetAISecondaryButton(text = "Rate Us", onClick = {
                val packageName = context.packageName
                val uri = "market://details?id=$packageName".toUri()
                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val webIntent = Intent(
                        Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName".toUri()
                    )
                    context.startActivity(webIntent)
                }
            }, modifier = Modifier.fillMaxWidth(), startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_rate), contentDescription = null)
            })
            PetAISecondaryButton(text = "Terms of Use", onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "https://www.yeahapps.me/terms".toUri())
                context.startActivity(intent)
            }, modifier = Modifier.fillMaxWidth(), startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_terms), contentDescription = null)
            })
            PetAISecondaryButton(text = "Privacy Policy", onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "http://yeahapps.me/privacy".toUri())
                context.startActivity(intent)
            }, modifier = Modifier.fillMaxWidth(), startContent = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_policy), contentDescription = null)
            })
        }
    }
}