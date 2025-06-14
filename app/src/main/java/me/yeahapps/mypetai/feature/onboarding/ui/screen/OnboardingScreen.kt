package me.yeahapps.mypetai.feature.onboarding.ui.screen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.PetAIDefaultShade
import me.yeahapps.mypetai.core.ui.component.PetAIVideoPlayer
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIPrimaryButton
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.ManagePlayerLifecycle
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.onboarding.ui.action.OnboardingAction
import me.yeahapps.mypetai.feature.onboarding.ui.event.OnboardingEvent
import me.yeahapps.mypetai.feature.onboarding.ui.state.OnboardingState
import me.yeahapps.mypetai.feature.onboarding.ui.viewmodel.OnboardingViewModel

@Serializable
object OnboardingScreen

@Composable
fun OnboardingContainer(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    navigateToSubscriptions: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = "android.resource://${context.packageName}/${R.raw.onboarding}".toUri()
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }

    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            OnboardingAction.CloseApp -> activity?.finish()
            OnboardingAction.NavigateToSubscriptionScreen -> navigateToSubscriptions()
            null -> {}
        }
    }

    ManagePlayerLifecycle(exoPlayer)
    OnboardingContent(
        modifier = modifier,
        exoPlayer = exoPlayer,
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@OptIn(UnstableApi::class)
@Composable
private fun OnboardingContent(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    state: OnboardingState = OnboardingState(),
    onEvent: (OnboardingEvent) -> Unit = {}
) {

    BackHandler {
        onEvent(OnboardingEvent.ShowPreviousSlide)
    }

    Box(modifier = modifier.navigationBarsPadding(), contentAlignment = Alignment.BottomCenter) {
        Box {
            PetAIVideoPlayer(exoPlayer = exoPlayer, modifier = Modifier.fillMaxSize())
            PetAIDefaultShade(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                color = PetAITheme.colors.buttonTextSecondary,
                style = PetAITheme.typography.headlineSemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                text = when (state.slideIndex) {
                    0 -> stringResource(R.string.onboarding_title_1)
                    1 -> stringResource(R.string.onboarding_title_2)
                    else -> ""
                }
            )
            Spacer(Modifier.size(12.dp))
            Text(
                color = PetAITheme.colors.buttonTextSecondary,
                style = PetAITheme.typography.textRegular,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                text = when (state.slideIndex) {
                    0 -> stringResource(R.string.onboarding_description_1)
                    1 -> stringResource(R.string.onboarding_description_2)
                    else -> ""
                }
            )
            Spacer(Modifier.size(24.dp))
            PetAIPrimaryButton(
                centerContent = stringResource(R.string.common_continue),
                onClick = { onEvent(OnboardingEvent.ShowNextSlide) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
            if (state.slideIndex == 1) {
                Spacer(Modifier.size(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides PetAITheme.colors.buttonTextSecondary,
                        LocalTextStyle provides PetAITheme.typography.labelRegular
                    ) {
                        val policyText =
                            AnnotatedString.fromHtml(htmlString = stringResource(R.string.common_privacy_policy))
                        val termsText =
                            AnnotatedString.fromHtml(htmlString = stringResource(R.string.common_terms_of_use))
                        Text(text = policyText)
                        Text(text = "|")
                        Text(text = termsText)
                        Text(text = "|")
                        Text(text = stringResource(R.string.common_limiter_version))
                    }
                }
            }
            Spacer(Modifier.size(if (state.slideIndex == 1) 16.dp else 36.dp))
        }
    }
}