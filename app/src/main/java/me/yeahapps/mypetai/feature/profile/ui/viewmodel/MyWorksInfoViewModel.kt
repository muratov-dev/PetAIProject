package me.yeahapps.mypetai.feature.profile.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import me.yeahapps.mypetai.feature.profile.ui.action.MyWorksInfoAction
import me.yeahapps.mypetai.feature.profile.ui.event.MyWorksInfoEvent
import me.yeahapps.mypetai.feature.profile.ui.screen.MyWorksInfoScreen
import me.yeahapps.mypetai.feature.profile.ui.state.MyWorksInfoState
import javax.inject.Inject

@HiltViewModel
class MyWorksInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val myWorksRepository: MyWorksRepository
) : BaseViewModel<MyWorksInfoState, MyWorksInfoEvent, MyWorksInfoAction>(MyWorksInfoState()) {

    val args = savedStateHandle.toRoute<MyWorksInfoScreen>()

    override fun obtainEvent(viewEvent: MyWorksInfoEvent) {
        when (viewEvent) {
            MyWorksInfoEvent.NavigateUp -> sendAction(MyWorksInfoAction.NavigateUp)
            MyWorksInfoEvent.DeleteWork -> viewModelScoped {
                myWorksRepository.deleteMyWork(args.workId)
            }
        }
    }

    init {
        viewModelScoped {
            val work = myWorksRepository.getMyWorkInfo(args.workId)
            updateViewState { copy(workInfo = work) }
        }
    }
}