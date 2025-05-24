package me.yeahapps.mypetai.feature.create.ui.state

import android.net.Uri

data class CreateState(
    val userImageUri: Uri? = null,
    val userAudioUri: Uri? = null
)
