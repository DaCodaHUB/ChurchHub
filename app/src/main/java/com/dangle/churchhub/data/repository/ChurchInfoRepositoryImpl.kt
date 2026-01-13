package com.dangle.churchhub.data.repository

import com.dangle.churchhub.core.util.parseIsoInstantToEpochMs
import com.dangle.churchhub.data.local.dao.ChurchInfoDao
import com.dangle.churchhub.data.local.entity.ChurchInfoEntity
import com.dangle.churchhub.data.remote.api.ChurchInfoApi
import com.dangle.churchhub.domain.repo.ChurchInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChurchInfoRepositoryImpl @Inject constructor(
    private val api: ChurchInfoApi,
    private val dao: ChurchInfoDao
) : ChurchInfoRepository {

    override fun observe(): Flow<ChurchInfoEntity?> = dao.observe()

    override suspend fun refresh(): Result<Unit> = runCatching {
        val dto = api.fetchChurchInfo()
        val updatedAtMs = parseIsoInstantToEpochMs(dto.updatedAt)

        val c = dto.church
        val entity = ChurchInfoEntity(
            id = 1,
            name = c.name,
            tagline = c.tagline,
            addressLine1 = c.address.line1,
            city = c.address.city,
            state = c.address.state,
            zip = c.address.zip,
            lat = c.address.lat,
            lng = c.address.lng,
            phone = c.contact?.phone,
            email = c.contact?.email,
            website = c.links?.website,
            giving = c.links?.giving,
            youtubeChannel = c.links?.youtubeChannel,
            instagram = c.links?.instagram,
            updatedAtEpochMs = updatedAtMs
        )
        dao.upsert(entity)
    }
}
