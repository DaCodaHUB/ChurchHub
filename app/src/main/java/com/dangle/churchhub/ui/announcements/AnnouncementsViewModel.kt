package com.dangle.churchhub.ui.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.domain.repo.AnnouncementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val repo: AnnouncementsRepository
) : ViewModel() {

    val announcements: StateFlow<List<AnnouncementEntity>> =
        repo.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun refresh() {
        viewModelScope.launch { repo.refresh() }
    }
}
