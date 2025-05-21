package me.yeahapps.mypetai.feature.profile.ui.state

import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel

data class MyWorksState(
    val works: List<MyWorkModel> = emptyList()
)
