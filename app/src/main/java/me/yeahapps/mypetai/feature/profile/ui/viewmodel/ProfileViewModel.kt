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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val myWorksRepository: MyWorksRepository, private val billingManager: BillingManager
) : ViewModel() {

    private val _myWorksCount = MutableStateFlow(0)
    val myWorksCount = _myWorksCount.asStateFlow()

    private val _hasSubscription = MutableStateFlow(false)
    val hasSubscription = _hasSubscription.asStateFlow()

    init {
        viewModelScope.launch {
            _myWorksCount.update { myWorksRepository.getMyWorksCount() }
        }
        viewModelScope.launch {
            billingManager.isSubscribed.collectLatest {
                _hasSubscription.update { it }
            }
        }
    }
}