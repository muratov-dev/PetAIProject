package me.yeahapps.mypetai.feature.create.ui.state

import android.net.Uri

data class CreateState(
    val isButtonEnabled: Boolean = false,
    val hasSubscription: Boolean = false,
    val isAudioPlaying: Boolean = false,
    val userImageUri: Uri? = null,
    val userAudioUri: Uri? = null
)
