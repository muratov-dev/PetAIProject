package me.yeahapps.mypetai.feature.subscriptions.ui.state

import me.yeahapps.mypetai.feature.subscriptions.domain.model.SubscriptionModel

data class SubscriptionsState(
    val subscriptionsList: List<SubscriptionModel> = emptyList()
)
