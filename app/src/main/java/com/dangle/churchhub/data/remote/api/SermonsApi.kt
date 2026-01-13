package com.dangle.churchhub.data.remote.api

import com.dangle.churchhub.data.remote.dto.SermonsResponseDto
import retrofit2.http.GET

interface SermonsApi {
    @GET("content/v1/sermons.json")
    suspend fun fetchSermons(): SermonsResponseDto
}
