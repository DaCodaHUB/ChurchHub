package com.dangle.churchhub.ui.readingplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.entity.ReadingPlanItemEntity
import com.dangle.churchhub.data.settings.SettingsRepository
import com.dangle.churchhub.domain.repo.ReadingPlanRepository
import com.dangle.churchhub.ui.settings.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReadingPlanRow(
    val item: ReadingPlanItemEntity,
    val isCompleted: Boolean
)

data class ReadingPlanUiState(
    val language: AppLanguage = AppLanguage.EN,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val planTitle: String = "",
    val planTitleVi: String = "",
    val rows: List<ReadingPlanRow> = emptyList()
)

@HiltViewModel
class ReadingPlanViewModel @Inject constructor(
    private val repo: ReadingPlanRepository,
    settingsRepo: SettingsRepository
) : ViewModel() {

    private val planFlow = repo.observePlan()
    private val completedIdsFlow = repo.observeCompletedIds()

    private val rowsFlow = combine(planFlow, completedIdsFlow) { plan, done ->
        plan.map { ReadingPlanRow(it, done.contains(it.id)) }
    }.distinctUntilChanged()

    private val titlesFlow = planFlow
        .map { plan ->
            val first = plan.firstOrNull()
            (first?.planTitle.orEmpty()) to (first?.planTitleVi.orEmpty())
        }
        .distinctUntilChanged()

    private val languageFlow = settingsRepo.settings
        .map { it.language }
        .distinctUntilChanged()

    // Refresh/error states kept simple
    private val refreshState = kotlinx.coroutines.flow.MutableStateFlow(false)
    private val errorState = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)

    val uiState: StateFlow<ReadingPlanUiState> =
        combine(languageFlow, refreshState, errorState, titlesFlow, rowsFlow) { lang, refreshing, err, titles, rows ->
            ReadingPlanUiState(
                language = lang,
                isRefreshing = refreshing,
                error = err,
                planTitle = titles.first,
                planTitleVi = titles.second,
                rows = rows
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReadingPlanUiState())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshState.value = true
            errorState.value = null
            val result = repo.refresh()
            refreshState.value = false
            errorState.value = result.exceptionOrNull()?.message
        }
    }

    fun setCompleted(itemId: String, completed: Boolean) {
        viewModelScope.launch { repo.setCompleted(itemId, completed) }
    }
}
