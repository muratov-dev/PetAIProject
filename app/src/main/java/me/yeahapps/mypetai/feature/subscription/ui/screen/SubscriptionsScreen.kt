package me.yeahapps.mypetai.feature.subscription.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.billingclient.api.ProductDetails
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIPrimaryButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.subscription.ui.action.SubscriptionsAction
import me.yeahapps.mypetai.feature.subscription.ui.component.SubscriptionItem
import me.yeahapps.mypetai.feature.subscription.ui.event.SubscriptionsEvent
import me.yeahapps.mypetai.feature.subscription.ui.state.SubscriptionsState
import me.yeahapps.mypetai.feature.subscription.ui.viewmodel.SubscriptionsViewModel
import kotlin.math.roundToInt

@Composable
fun SubscriptionsContainer(
    modifier: Modifier = Modifier, viewModel: SubscriptionsViewModel = hiltViewModel(), onScreenClose: () -> Unit
) {

    BackHandler { onScreenClose() }

    val context = LocalContext.current

    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            SubscriptionsAction.CloseScreen -> onScreenClose()
            SubscriptionsAction.RelativeSubscriptionActivated -> {
                Toast.makeText(context, "Subscription for relatives activated", Toast.LENGTH_SHORT).show()
            }
            null -> {}
        }
    }
    SubscriptionsContent(
        modifier = modifier.pointerInput(true) {},
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@Composable
private fun SubscriptionsContent(
    modifier: Modifier = Modifier,
    state: SubscriptionsState = SubscriptionsState(),
    onEvent: (SubscriptionsEvent) -> Unit = {}
) {
    val activity = LocalActivity.current
    var relativesSubscriptionCount by remember { mutableIntStateOf(0) }
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upgrade Plan Now!",
                style = PetAITheme.typography.headlineBold,
                color = PetAITheme.colors.textPrimary
            )
            Spacer(Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_bg),
                    contentDescription = null,
                    tint = PetAITheme.colors.buttonPrimaryDefault,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Unlimited Generate With AI",
                    textAlign = TextAlign.Start,
                    style = PetAITheme.typography.buttonTextRegular,
                    color = PetAITheme.colors.textPrimary
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_bg),
                    contentDescription = null,
                    tint = PetAITheme.colors.buttonPrimaryDefault,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Pictured Talking And Singing",
                    textAlign = TextAlign.Start,
                    style = PetAITheme.typography.buttonTextRegular,
                    color = PetAITheme.colors.textPrimary
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_bg),
                    contentDescription = null,
                    tint = PetAITheme.colors.buttonPrimaryDefault,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Faster Rendering",
                    textAlign = TextAlign.Start,
                    style = PetAITheme.typography.buttonTextRegular,
                    color = PetAITheme.colors.textPrimary,
                    modifier = Modifier.clickable(enabled = relativesSubscriptionCount != 20) {
                        relativesSubscriptionCount++
                        if(relativesSubscriptionCount == 20){
                            onEvent(SubscriptionsEvent.ActivateRelativesSubscription)
                        }
                    }
                )
            }
            Spacer(Modifier.size(16.dp))
            CompositionLocalProvider(LocalContentColor provides PetAITheme.colors.textPrimary) {
                if (state.subscriptionsList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No subscriptions available", style = PetAITheme.typography.headlineMedium)
                    }
                } else {
                    state.subscriptionsList.forEach { product ->
                        val offer = product.subscriptionOfferDetails?.firstOrNull() ?: return@forEach
                        val pricePerWeek = getWeeklyPrice(product)
                        val fullPrice = offer.pricingPhases.pricingPhaseList.firstOrNull()?.formattedPrice ?: "-"
                        val title = if (getWeeks(product) > 1) "Yearly Access" else "Weekly Access"
                        val subtitle = if (getWeeks(product) > 1) "Just $fullPrice per year" else "Cancel Anytime"

                        val discount = if (state.subscriptionsList.size == 2 && getWeeks(product) > 1) {
                            val weekly = state.subscriptionsList.firstOrNull { getWeeks(it) == 1 }
                            weekly?.let { calculateDiscountPercent(product, it) }
                        } else null

                        SubscriptionItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            title = title,
                            subtitle = subtitle,
                            weeklyPrice = pricePerWeek,
                            discountPercent = discount,
                            selected = state.selectedDetails == product,
                            onClick = { onEvent(SubscriptionsEvent.SelectSubscription(product)) })
                    }
                }
            }
            Spacer(Modifier.size(24.dp))
            Text(
                text = "Non commitment - cancel anytime",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.5f),
                style = PetAITheme.typography.buttonTextRegular
            )
            Spacer(Modifier.size(16.dp))
            PetAIPrimaryButton(
                centerContent = stringResource(R.string.common_continue),
                enabled = state.selectedDetails != null,
                onClick = {
                    activity?.let { onEvent(SubscriptionsEvent.LaunchPurchaseFlow(it, state.selectedDetails!!)) }
                },
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
            ),
            onClick = { onEvent(SubscriptionsEvent.CloseScreen) })
    }
}

fun getWeeklyPrice(product: ProductDetails): String {
    val offer = product.subscriptionOfferDetails?.firstOrNull() ?: return "-"
    val pricingPhase = offer.pricingPhases.pricingPhaseList.firstOrNull() ?: return "-"
    val micros = pricingPhase.priceAmountMicros
    val price = micros / 1_000_000.0
    val billingPeriod = pricingPhase.billingPeriod // e.g., "P1Y"

    val weeks = when {
        billingPeriod.contains("Y") -> 52
        billingPeriod.contains("M") -> 4
        billingPeriod.contains("W") -> 1
        else -> 1
    }

    val weekly = price / weeks
    return String.format("%.2f", weekly)
}

fun calculateDiscountPercent(base: ProductDetails, compared: ProductDetails): Int {
    val basePrice = extractMicros(base).toDouble()
    val weeksBase = getWeeks(base)
    val weeklyBase = basePrice / weeksBase

    val comparedPrice = extractMicros(compared).toDouble()
    val weeksCompared = getWeeks(compared)
    val weeklyCompared = comparedPrice / weeksCompared

    val discount = ((weeklyCompared - weeklyBase) / weeklyCompared) * 100
    return discount.roundToInt()
}

fun extractMicros(product: ProductDetails): Long {
    return product.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.priceAmountMicros
        ?: 0
}

fun getWeeks(product: ProductDetails): Int {
    val period =
        product.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.billingPeriod
            ?: "P1W"

    return when {
        period.contains("Y") -> 52
        period.contains("M") -> 4
        period.contains("W") -> 1
        else -> 1
    }
}