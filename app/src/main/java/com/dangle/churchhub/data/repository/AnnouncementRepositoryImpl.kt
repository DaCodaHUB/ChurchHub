package com.dangle.churchhub.data.repository

import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.mappers.toEntity
import com.dangle.churchhub.data.remote.api.AnnouncementsApi
import com.dangle.churchhub.domain.model.Announcement
import com.dangle.churchhub.domain.repo.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class AnnouncementRepositoryImpl @Inject constructor(
    private val api: AnnouncementsApi,
    private val dao: AnnouncementDao
) : AnnouncementRepository {

    override fun observeAnnouncements(): Flow<List<Announcement>> =
        dao.observeAll().map { entities ->
            entities.map { e ->
                Announcement(
                    id = e.id,
                    title = e.title,
                    bodyMarkdown = e.bodyMarkdown,
                    category = e.category,
                    pinned = e.pinned,
                    publishedAt = Instant.ofEpochMilli(e.publishedAtEpochMs),
                    updatedAt = Instant.ofEpochMilli(e.updatedAtEpochMs)
                )
            }
        }

    override suspend fun syncAnnouncements(): Result<Unit> = runCatching {
        val dto = api.fetchAnnouncements()
        val entities = dto.announcements.map { it.toEntity() }
        dao.upsertAll(entities)
    }
}