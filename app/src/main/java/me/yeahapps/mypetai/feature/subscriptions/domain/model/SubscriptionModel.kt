package me.yeahapps.mypetai.feature.subscriptions.domain.model

import com.android.billingclient.api.ProductDetails

data class SubscriptionModel(val isSelected: Boolean = false, val details: ProductDetails)
