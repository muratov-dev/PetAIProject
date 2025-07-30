package me.yeahapps.mypetai.feature.subscription.domain.utils

import com.android.billingclient.api.ProductDetails
import me.yeahapps.mypetai.feature.subscription.domain.model.SubscriptionModel

fun ProductDetails.toSubscriptionModel(
    allSubscriptions: List<ProductDetails>,
    selectedProduct: ProductDetails?,
    getWeeklyPrice: (ProductDetails) -> String,
    calculateDiscountPercent: (ProductDetails, ProductDetails) -> Int?
): SubscriptionModel {
    val offer = subscriptionOfferDetails?.firstOrNull()
        ?: return SubscriptionModel("Unknown", "-", "-", null, false, this)

    val pricingPhases = offer.pricingPhases.pricingPhaseList
    val mainPhase = pricingPhases.lastOrNull() ?: return SubscriptionModel("Unknown", "-", "-", null, false, this)

    val hasTrial = pricingPhases.size > 1 && pricingPhases.first().priceAmountMicros == 0L
    val billingPeriod = mainPhase.billingPeriod // e.g., "P1W", "P1M", "P1Y"
    val fullPrice = mainPhase.formattedPrice
    val weeks = parseWeeksFromPeriod(billingPeriod)

    val title = when {
        hasTrial -> "Free Trial (${pricingPhases.first().billingPeriod})"
        weeks >= 52 -> "Yearly Access"
        weeks >= 4 -> "Monthly Access"
        weeks == 1 -> "Weekly Access"
        else -> "Access for $weeks weeks"
    }

    val subtitle = when {
        hasTrial -> "Then $fullPrice"
        weeks >= 52 -> "Just $fullPrice per year"
        weeks >= 4 -> "Just $fullPrice per month"
        else -> "Cancel Anytime"
    }

    val discount = if (allSubscriptions.size >= 2 && weeks >= 4) {
        val weekly = allSubscriptions.firstOrNull { parseWeeksFromPeriod(it.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.lastOrNull()?.billingPeriod ?: "") == 1 }
        weekly?.let { calculateDiscountPercent(this, it) }
    } else null

    return SubscriptionModel(
        title = title,
        subtitle = subtitle,
        weeklyPrice = getWeeklyPrice(this),
        discountPercent = discount,
        isSelected = selectedProduct == this,
        product = this
    )
}

fun parseWeeksFromPeriod(period: String): Int {
    val regex = Regex("""P(\d+)W""")
    val match = regex.find(period)
    if (match != null) return match.groupValues[1].toInt()

    val monthsMatch = Regex("""P(\d+)M""").find(period)
    if (monthsMatch != null) return monthsMatch.groupValues[1].toInt() * 4

    val yearsMatch = Regex("""P(\d+)Y""").find(period)
    if (yearsMatch != null) return yearsMatch.groupValues[1].toInt() * 52

    return 1 // fallback
}
