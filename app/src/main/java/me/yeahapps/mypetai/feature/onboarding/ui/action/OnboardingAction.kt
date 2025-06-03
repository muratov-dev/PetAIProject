package me.yeahapps.mypetai.feature.onboarding.ui.action

sealed interface OnboardingAction {

    data object CloseApp : OnboardingAction
    data object NavigateToSubscriptionScreen : OnboardingAction
}