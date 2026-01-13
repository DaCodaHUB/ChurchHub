package com.dangle.churchhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.entity.ChurchInfoEntity
import com.dangle.churchhub.domain.repo.ChurchInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ChurchInfoRepository
) : ViewModel() {

    val churchInfo: StateFlow<ChurchInfoEntity?> =
        repo.observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun refresh() {
        viewModelScope.launch { repo.refresh() }
    }
}
