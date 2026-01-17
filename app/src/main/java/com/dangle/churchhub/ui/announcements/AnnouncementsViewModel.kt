package com.dangle.churchhub.ui.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.domain.repo.AnnouncementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnnouncementsUiState(
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val generatedAt: String = "",
    val bulletinJson: String? = null
)

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val repo: AnnouncementsRepository
) : ViewModel() {

    private val refreshing = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AnnouncementsUiState> =
        combine(repo.observeBulletin(), refreshing, error) { entity, isRefreshing, err ->
            AnnouncementsUiState(
                isRefreshing = isRefreshing,
                error = err,
                generatedAt = entity?.generatedAt.orEmpty(),
                bulletinJson = entity?.json
            )
        }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), AnnouncementsUiState())

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            refreshing.value = true
            error.value = null
            val result = repo.refresh()
            refreshing.value = false
            error.value = result.exceptionOrNull()?.message
        }
    }
}
