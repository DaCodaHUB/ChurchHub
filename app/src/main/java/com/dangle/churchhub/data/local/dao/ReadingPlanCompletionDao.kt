package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.ReadingPlanCompletedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingPlanCompletionDao {
    @Query("SELECT itemId FROM reading_plan_completed")
    fun observeCompletedIds(): Flow<List<String>>

    @Upsert
    suspend fun markCompleted(entity: ReadingPlanCompletedEntity)

    @Query("DELETE FROM reading_plan_completed WHERE itemId = :itemId")
    suspend fun unmarkCompleted(itemId: String)
}
