package com.dangle.churchhub.ui.readingplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.entity.ReadingPlanItemEntity
import com.dangle.churchhub.domain.repo.ReadingPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReadingPlanRow(
    val item: ReadingPlanItemEntity,
    val isCompleted: Boolean
)

data class ReadingPlanUiState(
    val language: Language = Language.EN,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val planTitle: String = "",
    val planTitleVi: String = "",
    val rows: List<ReadingPlanRow> = emptyList()
)

@HiltViewModel
class ReadingPlanViewModel @Inject constructor(
    private val repo: ReadingPlanRepository
) : ViewModel() {

    // Language toggle lives in the VM so it’s easy to reuse across screens later.
    private val language = MutableStateFlow(Language.EN)

    // Refresh/error state
    private val refreshing = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    // Data streams from Room
    private val planFlow = repo.observePlan()
    private val completedIdsFlow = repo.observeCompletedIds()

    // Combine plan + completion into rows
    private val rowsFlow = combine(planFlow, completedIdsFlow) { plan, completedIds ->
        plan.map { item -> ReadingPlanRow(item = item, isCompleted = completedIds.contains(item.id)) }
    }.distinctUntilChanged()

    // Derive titles from the first item (since each entity stores plan titles)
    private val titlesFlow = planFlow.mapTitles()

    // Public UI state
    val uiState: StateFlow<ReadingPlanUiState> =
        combine(
            language,
            refreshing,
            error,
            titlesFlow,
            rowsFlow
        ) { lang, isRefreshing, err, titles, rows ->
            ReadingPlanUiState(
                language = lang,
                isRefreshing = isRefreshing,
                error = err,
                planTitle = titles.first,
                planTitleVi = titles.second,
                rows = rows
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReadingPlanUiState())

    fun setLanguage(lang: Language) {
        language.value = lang
    }

    fun toggleLanguage() {
        language.value = if (language.value == Language.EN) Language.VI else Language.EN
    }

    fun refresh() {
        viewModelScope.launch {
            refreshing.value = true
            error.value = null

            val result = repo.refresh()
            refreshing.value = false
            error.value = result.exceptionOrNull()?.message
        }
    }

    fun setCompleted(itemId: String, completed: Boolean) {
        viewModelScope.launch {
            repo.setCompleted(itemId, completed)
        }
    }
}

/**
 * Helper to derive (planTitle, planTitleVi) from the plan list.
 * Assumes your entity stores both titles (from reading_plan.json). :contentReference[oaicite:1]{index=1}
 */
private fun kotlinx.coroutines.flow.Flow<List<ReadingPlanItemEntity>>.mapTitles():
        kotlinx.coroutines.flow.Flow<Pair<String, String>> =
    this.combine(MutableStateFlow(Unit)) { plan, _ ->
        val first = plan.firstOrNull()
        val en = first?.planTitle.orEmpty()
        val vi = first?.planTitleVi.orEmpty()
        en to vi
    }.distinctUntilChanged()
