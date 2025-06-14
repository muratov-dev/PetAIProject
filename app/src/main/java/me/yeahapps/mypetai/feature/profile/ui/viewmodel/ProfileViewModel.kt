package me.yeahapps.mypetai.feature.profile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.yeahapps.mypetai.core.data.BillingManager
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import me.yeahapps.mypetai.feature.profile.ui.state.ProfileState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val myWorksRepository: MyWorksRepository, private val billingManager: BillingManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val worksCount = myWorksRepository.getMyWorksCount()
            _state.update { it.copy(myWorksCount = worksCount) }
        }
        viewModelScope.launch {
            billingManager.isSubscribed.collectLatest { hasSubscription ->
                _state.update { it.copy(hasSubscription = hasSubscription) }
            }
        }
    }
}