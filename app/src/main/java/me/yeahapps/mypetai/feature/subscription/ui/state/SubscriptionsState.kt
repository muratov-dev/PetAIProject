package me.yeahapps.mypetai.feature.subscription.ui.state

import com.android.billingclient.api.ProductDetails

data class SubscriptionsState(
    val subscriptionsList: List<ProductDetails> = emptyList(),
    val selectedDetails: ProductDetails? = null
)
