package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.SermonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SermonDao {
    @Query("SELECT * FROM sermons ORDER BY dateEpochMs DESC")
    fun observeAll(): Flow<List<SermonEntity>>

    @Query("SELECT * FROM sermons WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<SermonEntity?>

    @Query("""
    SELECT * FROM sermons
    WHERE title LIKE '%' || :query || '%'
       OR speaker LIKE '%' || :query || '%'
       OR series LIKE '%' || :query || '%'
    ORDER BY dateEpochMs DESC
  """)

    fun observeSearch(query: String): Flow<List<SermonEntity>>

    @Upsert
    suspend fun upsertAll(items: List<SermonEntity>)
}
