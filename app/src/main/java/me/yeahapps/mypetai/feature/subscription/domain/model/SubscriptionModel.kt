package me.yeahapps.mypetai.feature.subscription.domain.model

import com.android.billingclient.api.ProductDetails

data class SubscriptionModel(val id: Int, val isSelected: Boolean = false, val details: ProductDetails)
