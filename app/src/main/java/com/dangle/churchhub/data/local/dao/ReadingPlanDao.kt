package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.ReadingPlanItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingPlanDao {
    @Query("SELECT * FROM reading_plan_items ORDER BY dateEpochMs ASC")
    fun observeAll(): Flow<List<ReadingPlanItemEntity>>

    @Upsert
    suspend fun upsertAll(items: List<ReadingPlanItemEntity>)

    @Query("DELETE FROM reading_plan_items")
    suspend fun clear()
}
