package me.yeahapps.mypetai.feature.profile.ui.state

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileState(
    val myWorksCount: Int = 0,
    val hasSubscription: Boolean = false
)
