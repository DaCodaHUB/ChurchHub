package com.dangle.churchhub.ui.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.domain.repo.AnnouncementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnnouncementsUiState(
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val selectedCategory: String? = null,   // null = All
    val categories: List<String> = emptyList(),
    val items: List<AnnouncementEntity> = emptyList()
)

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val repo: AnnouncementsRepository
) : ViewModel() {

    private val isRefreshing = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)
    private val selectedCategory = MutableStateFlow<String?>(null)

    private val allAnnouncements = repo.observeAll()
        .map { list ->
            // Consistent ordering: pinned first, newest first
            list.sortedWith(
                compareByDescending<AnnouncementEntity> { it.pinned }
                    .thenByDescending { it.publishedAtEpochMs }
            )
        }
        .distinctUntilChanged()

    private val categories = allAnnouncements
        .map { list ->
            list.map { it.category }
                .distinct()
                .sorted()
        }
        .distinctUntilChanged()

    private val filteredAnnouncements = combine(allAnnouncements, selectedCategory) { list, cat ->
        if (cat.isNullOrBlank()) list else list.filter { it.category == cat }
    }.distinctUntilChanged()

    val uiState: StateFlow<AnnouncementsUiState> =
        combine(
            isRefreshing,
            error,
            selectedCategory,
            categories,
            filteredAnnouncements
        ) { refreshing, err, selected, cats, items ->
            AnnouncementsUiState(
                isRefreshing = refreshing,
                error = err,
                selectedCategory = selected,
                categories = cats,
                items = items
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AnnouncementsUiState())

    init {
        // Optional: auto-refresh once when entering screen
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            error.value = null

            val result = repo.refresh()

            isRefreshing.value = false
            error.value = result.exceptionOrNull()?.message
        }
    }

    fun selectCategory(category: String?) {
        selectedCategory.value = category
    }

    fun clearError() {
        error.value = null
    }
}
