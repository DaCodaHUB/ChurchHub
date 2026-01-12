package com.dangle.churchhub.domain.repo

import com.dangle.churchhub.domain.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun observeAnnouncements(): Flow<List<Announcement>>
    suspend fun syncAnnouncements(): Result<Unit>
}