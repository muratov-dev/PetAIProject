package me.yeahapps.mypetai.feature.subscriptions.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIPrimaryButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.feature.subscriptions.ui.component.SubscriptionItem
import me.yeahapps.mypetai.feature.subscriptions.ui.event.SubscriptionsEvent
import me.yeahapps.mypetai.feature.subscriptions.ui.state.SubscriptionsState
import me.yeahapps.mypetai.feature.subscriptions.ui.viewmodel.SubscriptionsViewModel

@Serializable
object SubscriptionsScreen

@Composable
fun SubscriptionsContainer(modifier: Modifier = Modifier, viewModel: SubscriptionsViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    SubscriptionsContent(
        modifier = modifier, state = state, onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@Composable
private fun SubscriptionsContent(
    modifier: Modifier = Modifier,
    state: SubscriptionsState = SubscriptionsState(),
    onEvent: (SubscriptionsEvent) -> Unit = {}
) {

    Box(modifier = modifier.navigationBarsPadding()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            Image(
                painter = painterResource(R.drawable.im_onboarding_subscription_bg),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter)
                    .drawWithCache {
                        val gradient = Brush.linearGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent, 0.22f to Color(0xA6040400), 1.0f to Color(0xFF040400)
                            ), start = Offset(0f, 0f), end = Offset(0f, size.height)
                        )
                        onDrawBehind {
                            drawRect(brush = gradient)
                        }
                    })
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)) {
            CompositionLocalProvider(LocalContentColor provides PetAITheme.colors.textPrimary) {
                state.subscriptionsList.forEach { details ->
                    SubscriptionItem(
                        details.details,
                        isSelected = details.isSelected,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Yearly Access")
                        Text(text = "Just $49.99 per year")
                    }
                    Text(text = "89 % OFF", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                    Column() {
                        Text(text = "$0.96", textAlign = TextAlign.End)
                        Text(text = "per week", textAlign = TextAlign.End)
                    }
                }
            }
            Spacer(Modifier.size(24.dp))
            Text(
                text = "auto-renewal, cancel anytime",
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.5f),
                style = PetAITheme.typography.buttonTextRegular
            )
            Spacer(Modifier.size(16.dp))
            PetAIPrimaryButton(
                centerContent = stringResource(R.string.common_continue),
                onClick = { },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
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
                    val termsText = AnnotatedString.fromHtml(htmlString = stringResource(R.string.common_terms_of_use))
                    Text(text = policyText)
                    Text(text = "|")
                    Text(text = termsText)
                    Text(text = "|")
                    Text(text = stringResource(R.string.common_limiter_version))
                }
            }
            Spacer(Modifier.size(16.dp))
        }
        PetAIIconButton(
            icon = R.drawable.ic_close,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.TopStart),
            colors = PetAIIconButtonDefaults.colors(
                containerColor = PetAITheme.colors.buttonSecondaryDefault,
                contentColor = PetAITheme.colors.buttonTextPrimary
            )
        )
    }
}