package com.dangle.churchhub.domain.repo

import com.dangle.churchhub.data.local.entity.ChurchInfoEntity
import kotlinx.coroutines.flow.Flow

interface ChurchInfoRepository {
    fun observe(): Flow<ChurchInfoEntity?>
    suspend fun refresh(): Result<Unit>
}
