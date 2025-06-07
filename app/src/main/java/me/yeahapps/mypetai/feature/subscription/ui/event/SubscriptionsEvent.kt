package me.yeahapps.mypetai.feature.subscription.ui.event

import android.app.Activity
import com.android.billingclient.api.ProductDetails

sealed interface SubscriptionsEvent {

    data class SelectSubscription(val details: ProductDetails) : SubscriptionsEvent
    data class LaunchPurchaseFlow(val activity: Activity, val details: ProductDetails) : SubscriptionsEvent
    data object CloseScreen : SubscriptionsEvent
}