package dev.ymuratov.petai.feature.profile.ui.state

import dev.ymuratov.petai.feature.profile.domain.model.MyWorkModel

data class MyWorksState(
    val works: List<MyWorkModel> = emptyList()
)
