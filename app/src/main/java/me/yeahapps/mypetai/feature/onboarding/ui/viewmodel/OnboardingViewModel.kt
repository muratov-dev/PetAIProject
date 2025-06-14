package me.yeahapps.mypetai.feature.onboarding.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.onboarding.ui.action.OnboardingAction
import me.yeahapps.mypetai.feature.onboarding.ui.event.OnboardingEvent
import me.yeahapps.mypetai.feature.onboarding.ui.state.OnboardingState
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
) : BaseViewModel<OnboardingState, OnboardingEvent, OnboardingAction>(OnboardingState()) {

    override fun obtainEvent(viewEvent: OnboardingEvent) {
        when (viewEvent) {
            OnboardingEvent.ShowNextSlide -> showNextSlide()
            OnboardingEvent.ShowPreviousSlide -> showPreviousSlide()
        }
    }

    private fun showNextSlide() {
        val nextSlide = currentState.slideIndex + 1
        if (nextSlide > 1) sendAction(OnboardingAction.NavigateToSubscriptionScreen)
        else updateViewState { copy(slideIndex = nextSlide) }
    }

    private fun showPreviousSlide() {
        val previousSlide = currentState.slideIndex - 1
        if (previousSlide < 0) sendAction(OnboardingAction.CloseApp)
        else updateViewState { copy(slideIndex = previousSlide) }
    }
}