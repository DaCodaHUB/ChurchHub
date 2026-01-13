package com.dangle.churchhub.ui.readingplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.entity.ReadingPlanItemEntity
import com.dangle.churchhub.domain.repo.ReadingPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReadingPlanRow(
    val item: ReadingPlanItemEntity,
    val isCompleted: Boolean
)

@HiltViewModel
class ReadingPlanViewModel @Inject constructor(
    private val repo: ReadingPlanRepository
) : ViewModel() {

    val rows: StateFlow<List<ReadingPlanRow>> =
        combine(repo.observePlan(), repo.observeCompletedIds()) { plan, done ->
            plan.map { ReadingPlanRow(it, done.contains(it.id)) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun refresh() {
        viewModelScope.launch { repo.refresh() }
    }

    fun setCompleted(itemId: String, completed: Boolean) {
        viewModelScope.launch { repo.setCompleted(itemId, completed) }
    }
}
