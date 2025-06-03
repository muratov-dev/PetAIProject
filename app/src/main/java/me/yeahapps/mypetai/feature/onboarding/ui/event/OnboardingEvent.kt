package me.yeahapps.mypetai.feature.onboarding.ui.event

sealed interface OnboardingEvent {

    data object ShowNextSlide : OnboardingEvent
    data object ShowPreviousSlide : OnboardingEvent
}