package com.dangle.churchhub.data.repository

import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.data.remote.api.AnnouncementsApi
import com.dangle.churchhub.data.remote.dto.AnnouncementsDto
import com.dangle.churchhub.domain.repo.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AnnouncementsRepositoryImpl @Inject constructor(
    private val api: AnnouncementsApi,
    private val dao: AnnouncementDao,
    private val json: Json
) : AnnouncementsRepository {

    override fun observeBulletin(): Flow<AnnouncementEntity?> = dao.observe()

    override suspend fun refresh(): Result<Unit> = runCatching {
        val dto: AnnouncementsDto = api.fetchAnnouncements()

        val rawJson = json.encodeToString(AnnouncementsDto.serializer(), dto)

        dao.upsert(
            AnnouncementEntity(
                id = 1,
                generatedAt = dto.generatedAt,
                json = rawJson,
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }
}
