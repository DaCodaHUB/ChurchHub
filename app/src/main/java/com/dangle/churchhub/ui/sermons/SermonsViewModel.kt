package com.dangle.churchhub.ui.sermons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.domain.repo.SermonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SermonsViewModel @Inject constructor(
    private val repo: SermonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SermonsUiState())
    val uiState: StateFlow<SermonsUiState> = _uiState

    init {
        viewModelScope.launch {
            // Re-collect whenever query changes (simple approach for MVP)
            _uiState.collectLatest { state ->
                repo.observeSermons(state.query).collect { list ->
                    _uiState.update { it.copy(items = list) }
                }
            }
        }
        refresh()
    }

    fun onQueryChange(q: String) {
        _uiState.update { it.copy(query = q) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            val result = repo.syncSermons()
            _uiState.update {
                it.copy(isRefreshing = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
}
