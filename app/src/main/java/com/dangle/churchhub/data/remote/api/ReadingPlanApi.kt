package com.dangle.churchhub.data.remote.api

import com.dangle.churchhub.data.remote.dto.ReadingPlanResponseDto
import retrofit2.http.GET

interface ReadingPlanApi {
    @GET("content/v1/reading_plan.json")
    suspend fun fetchReadingPlan(): ReadingPlanResponseDto
}
