package me.yeahapps.mypetai.feature.profile.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import me.yeahapps.mypetai.feature.profile.ui.action.MyWorksAction
import me.yeahapps.mypetai.feature.profile.ui.event.MyWorksEvent
import me.yeahapps.mypetai.feature.profile.ui.state.MyWorksState
import javax.inject.Inject

@HiltViewModel
class MyWorksViewModel @Inject constructor(
    private val myWorksRepository: MyWorksRepository
) : BaseViewModel<MyWorksState, MyWorksEvent, MyWorksAction>(initialState = MyWorksState()) {

    override fun obtainEvent(viewEvent: MyWorksEvent) {
        when (viewEvent) {
            MyWorksEvent.NavigateUp -> sendAction(MyWorksAction.NavigateUp)
            is MyWorksEvent.NavigateToInfo -> sendAction(MyWorksAction.NavigateToInfo(viewEvent.id))
        }
    }

    init {
        viewModelScoped {
            myWorksRepository.getMyWorks().collectLatest { works ->
                updateViewState { copy(works = works) }
            }
        }
    }
}