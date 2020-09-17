package com.bell.youtubeplayer.repository.core

import com.bell.youtubeplayer.models.YouTubeResponse
import com.bell.youtubeplayer.utils.NetworkConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET(NetworkConstants.SEARCH)
    suspend fun searchTrack(
        @Query("q") term: String?,
        @Query("type") type: String?,
        @Query("key") key: String?,
        @Query("part") part: String?,
        @Query("maxResults") maxResults: Int?,
        @Query("videoEmbeddable") videoEmbeddable: String?,
        @Query("videoSyndicated") videoSyndicated: String?
    ): YouTubeResponse

    @GET(NetworkConstants.SEARCH)
    suspend fun searchTrack(
        @Query("q") term: String?,
        @Query("type") type: String?,
        @Query("key") key: String?,
        @Query("part") part: String?,
        @Query("maxResults") maxResults: Int?,
        @Query("pageToken") pageToken: String?,
        @Query("videoEmbeddable") videoEmbeddable: String?,
        @Query("videoSyndicated") videoSyndicated: String?
    ): YouTubeResponse
}