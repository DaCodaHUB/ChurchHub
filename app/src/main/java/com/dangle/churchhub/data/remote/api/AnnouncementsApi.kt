package com.dangle.churchhub.data.remote.api

import com.dangle.churchhub.data.remote.dto.AnnouncementsDto
import retrofit2.http.GET

interface AnnouncementsApi {
    @GET("content/v1/announcements.json")
    suspend fun fetchAnnouncements(): AnnouncementsDto
}
