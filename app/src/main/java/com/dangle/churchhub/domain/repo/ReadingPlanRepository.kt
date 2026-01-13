package com.dangle.churchhub.domain.repo

import com.dangle.churchhub.data.local.entity.ReadingPlanItemEntity
import kotlinx.coroutines.flow.Flow

interface ReadingPlanRepository {
    fun observePlan(): Flow<List<ReadingPlanItemEntity>>
    fun observeCompletedIds(): Flow<Set<String>>
    suspend fun refresh(): Result<Unit>
    suspend fun setCompleted(itemId: String, completed: Boolean)
}
