package com.dangle.churchhub.data.repository

import com.dangle.churchhub.core.util.parseLocalDateToEpochMs
import com.dangle.churchhub.data.local.dao.ReadingPlanCompletionDao
import com.dangle.churchhub.data.local.dao.ReadingPlanDao
import com.dangle.churchhub.data.local.entity.ReadingPlanCompletedEntity
import com.dangle.churchhub.data.local.entity.ReadingPlanItemEntity
import com.dangle.churchhub.data.remote.api.ReadingPlanApi
import com.dangle.churchhub.domain.repo.ReadingPlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadingPlanRepositoryImpl @Inject constructor(
    private val api: ReadingPlanApi,
    private val planDao: ReadingPlanDao,
    private val completionDao: ReadingPlanCompletionDao
) : ReadingPlanRepository {

    override fun observePlan(): Flow<List<ReadingPlanItemEntity>> = planDao.observeAll()

    override fun observeCompletedIds(): Flow<Set<String>> =
        completionDao.observeCompletedIds().map { it.toSet() }

    override suspend fun refresh(): Result<Unit> = runCatching {
        val dto = api.fetchReadingPlan()
        val now = System.currentTimeMillis()

        val entities = dto.items.map { i ->
            ReadingPlanItemEntity(
                id = i.id,
                dateEpochMs = parseLocalDateToEpochMs(i.date),
                dayNumber = i.dayNumber,
                reading = i.reading,
                psalm = i.psalm,
                planTitle = dto.planTitle,
                updatedAtEpochMs = now
            )
        }

        planDao.clear()
        planDao.upsertAll(entities)
    }

    override suspend fun setCompleted(itemId: String, completed: Boolean) {
        if (completed) {
            completionDao.markCompleted(
                ReadingPlanCompletedEntity(
                    itemId = itemId,
                    completedAtEpochMs = System.currentTimeMillis()
                )
            )
        } else {
            completionDao.unmarkCompleted(itemId)
        }
    }
}
