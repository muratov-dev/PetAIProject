package me.yeahapps.mypetai.feature.subscription.domain.model

import com.android.billingclient.api.ProductDetails

data class SubscriptionModel(
    val title: String,
    val subtitle: String,
    val weeklyPrice: String,
    val discountPercent: Int?,
    val isSelected: Boolean = false,
    val product: ProductDetails
)


