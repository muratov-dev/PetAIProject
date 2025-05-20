package dev.ymuratov.petai.feature.onboarding.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import dev.ymuratov.petai.R
import dev.ymuratov.petai.core.ui.component.button.filled.PetAIPrimaryButton
import dev.ymuratov.petai.core.ui.theme.PetAITheme
import kotlinx.serialization.Serializable

@Serializable
object OnboardingScreen

@Composable
fun OnboardingContainer(modifier: Modifier = Modifier, navigateToSubsOnboarding: () -> Unit) {
    OnboardingContent(modifier = modifier, navigateToSubsOnboarding = navigateToSubsOnboarding)
}

@Composable
private fun OnboardingContent(modifier: Modifier = Modifier, navigateToSubsOnboarding: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }

    Box(modifier = modifier.navigationBarsPadding(), contentAlignment = Alignment.BottomCenter) {
        Box {
            Image(
                painter = painterResource(R.drawable.im_onboarding_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).align(Alignment.BottomCenter).drawWithCache {
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
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                color = PetAITheme.colors.buttonTextSecondary,
                style = PetAITheme.typography.headlineSemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                text = when (step) {
                    0 -> stringResource(R.string.onboarding_title_1)
                    1 -> stringResource(R.string.onboarding_title_2)
                    2 -> stringResource(R.string.onboarding_title_3)
                    else -> ""
                }
            )
            Spacer(Modifier.size(12.dp))
            Text(
                color = PetAITheme.colors.buttonTextSecondary,
                style = PetAITheme.typography.textRegular,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                text = when (step) {
                    0 -> stringResource(R.string.onboarding_description_1)
                    1 -> stringResource(R.string.onboarding_description_2)
                    2 -> stringResource(R.string.onboarding_description_3)
                    else -> ""
                }
            )
            Spacer(Modifier.size(24.dp))
            PetAIPrimaryButton(
                centerContent = stringResource(R.string.common_continue),
                onClick = { if (step == 2) navigateToSubsOnboarding() else step++ },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            )
            if (step == 2) {
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
            Spacer(Modifier.size(if (step == 2) 16.dp else 36.dp))
        }
    }
}