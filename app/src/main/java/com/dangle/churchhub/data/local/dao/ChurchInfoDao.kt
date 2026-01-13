package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.ChurchInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChurchInfoDao {
    @Query("SELECT * FROM church_info WHERE id = 1 LIMIT 1")
    fun observe(): Flow<ChurchInfoEntity?>

    @Upsert
    suspend fun upsert(entity: ChurchInfoEntity)
}
