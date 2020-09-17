package com.bell.youtubeplayer.repository

import com.bell.youtubeplayer.models.YouTubeResponse
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItemListResponse
import com.google.api.services.youtube.model.PlaylistListResponse
import com.google.api.services.youtube.model.VideoListResponse

interface INetworkRepository {

    fun getPlayList(youTube: YouTube, pageToken: String): PlaylistListResponse?

    fun getPlayListDetails(
        youTube: YouTube,
        playLIstId: String,
        pageToken: String
    ): PlaylistItemListResponse

    fun getVideoDetail(youTube: YouTube, videoId: String): VideoListResponse

    suspend fun searchTrack(query: String, apiKey: String, pageToken: String): YouTubeResponse
}