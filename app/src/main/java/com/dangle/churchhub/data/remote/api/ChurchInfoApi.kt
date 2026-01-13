package com.dangle.churchhub.data.remote.api

import com.dangle.churchhub.data.remote.dto.ChurchInfoResponseDto
import retrofit2.http.GET

interface ChurchInfoApi {
    @GET("content/v1/church_info.json")
    suspend fun fetchChurchInfo(): ChurchInfoResponseDto
}
