package com.dangle.churchhub.ui.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.domain.repo.AnnouncementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val repo: AnnouncementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnnouncementsUiState())
    val uiState: StateFlow<AnnouncementsUiState> = _uiState

    init {
        viewModelScope.launch {
            repo.observeAnnouncements().collect { list ->
                _uiState.update { it.copy(items = list) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            val result = repo.syncAnnouncements()
            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}