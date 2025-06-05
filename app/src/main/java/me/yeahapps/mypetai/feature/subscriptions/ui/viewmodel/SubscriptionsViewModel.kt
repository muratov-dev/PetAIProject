package me.yeahapps.mypetai.feature.subscriptions.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import me.yeahapps.mypetai.core.data.BillingManager
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.subscriptions.domain.model.SubscriptionModel
import me.yeahapps.mypetai.feature.subscriptions.ui.action.SubscriptionsAction
import me.yeahapps.mypetai.feature.subscriptions.ui.event.SubscriptionsEvent
import me.yeahapps.mypetai.feature.subscriptions.ui.state.SubscriptionsState
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val billingManager: BillingManager
) : BaseViewModel<SubscriptionsState, SubscriptionsEvent, SubscriptionsAction>(SubscriptionsState()) {

    override fun obtainEvent(viewEvent: SubscriptionsEvent) {
        when (viewEvent) {
            else -> {}
        }
    }

    init {
        viewModelScoped {
            billingManager.availableSubscriptions.collectLatest { subscriptions ->
                val subscriptionsList = subscriptions.map { SubscriptionModel(details = it) }.toMutableList()
                if (subscriptionsList.isNotEmpty()) {
                    subscriptionsList.first().copy(isSelected = true)
                }
                updateViewState { copy(subscriptionsList = subscriptionsList) }
            }
        }
    }
}