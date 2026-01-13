package com.dangle.churchhub.domain.repo



import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

interface AnnouncementsRepository {
    fun observeAll(): Flow<List<AnnouncementEntity>>
    suspend fun refresh(): Result<Unit>
}
