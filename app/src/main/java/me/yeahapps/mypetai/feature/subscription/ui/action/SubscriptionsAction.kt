package me.yeahapps.mypetai.feature.subscription.ui.action


sealed interface SubscriptionsAction {
    data object CloseScreen : SubscriptionsAction
    data object RelativeSubscriptionActivated : SubscriptionsAction
}