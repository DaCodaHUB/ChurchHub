package com.dangle.churchhub.data.remote.api

import com.dangle.churchhub.data.remote.dto.SermonsYouTubeResponseDto
import retrofit2.http.GET

interface SermonsYouTubeApi {
    @GET("content/v1/sermons_youtube.json")
    suspend fun fetchYouTubeSermons(): SermonsYouTubeResponseDto
}
