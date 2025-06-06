package me.yeahapps.mypetai.feature.subscription.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import me.yeahapps.mypetai.core.data.BillingManager
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.subscription.ui.action.SubscriptionsAction
import me.yeahapps.mypetai.feature.subscription.ui.event.SubscriptionsEvent
import me.yeahapps.mypetai.feature.subscription.ui.state.SubscriptionsState
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val billingManager: BillingManager
) : BaseViewModel<SubscriptionsState, SubscriptionsEvent, SubscriptionsAction>(SubscriptionsState()) {

    override fun obtainEvent(viewEvent: SubscriptionsEvent) {
        when (viewEvent) {
            is SubscriptionsEvent.LaunchPurchaseFlow -> {
                billingManager.launchPurchaseFlow(viewEvent.activity, viewEvent.details)
            }

            is SubscriptionsEvent.SelectSubscription -> updateViewState { copy(selectedDetails = viewEvent.details) }

            SubscriptionsEvent.NavigateToRoot -> sendAction(SubscriptionsAction.NavigateToRoot)
            SubscriptionsEvent.NavigateUp -> sendAction(SubscriptionsAction.NavigateUp)
        }
    }

    init {
        viewModelScoped {
            billingManager.availableSubscriptions.collectLatest { subscriptions ->
                updateViewState { copy(subscriptionsList = subscriptions) }
            }
        }
    }
}