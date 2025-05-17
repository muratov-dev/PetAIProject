package dev.ymuratov.petai.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : Any, Event, Action>(initialState: State) : ViewModel() {
    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState.asStateFlow()

    protected val currentState: State
        get() = viewState.value

    private val _viewActions = Channel<Action?>(Channel.BUFFERED)
    val viewActions: Flow<Action?> = _viewActions.receiveAsFlow()

    protected fun updateViewState(block: State.() -> State) {
        _viewState.update(block)
    }

    protected fun sendAction(action: Action) {
        _viewActions.trySend(action)
    }

    abstract fun obtainEvent(viewEvent: Event)

    protected fun viewModelScoped(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(block = block)
    }
}