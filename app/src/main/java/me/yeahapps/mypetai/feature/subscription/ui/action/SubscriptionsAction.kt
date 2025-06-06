package me.yeahapps.mypetai.feature.subscription.ui.action


sealed interface SubscriptionsAction {
    data object NavigateUp : SubscriptionsAction
    data object NavigateToRoot : SubscriptionsAction
}