package com.bell.youtubeplayer.repository

import com.bell.youtubeplayer.models.YouTubeResponse
import com.bell.youtubeplayer.repository.core.APIService
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItemListResponse
import com.google.api.services.youtube.model.PlaylistListResponse
import com.google.api.services.youtube.model.VideoListResponse

class NetworkRepository(private val apiService: APIService) : INetworkRepository {

    override fun getPlayList(youTube: YouTube, pageToken: String): PlaylistListResponse? {
        return if (pageToken.isNotEmpty()) {
            youTube.playlists().list("contentDetails,snippet")
                .setMine(true)
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .execute()
        } else {
            youTube.playlists().list("contentDetails,snippet")
                .setMine(true)
                .setMaxResults(50L)
                .execute()
        }
    }

    override fun getPlayListDetails(
        youTube: YouTube,
        playLIstId: String,
        pageToken: String
    ): PlaylistItemListResponse {
        val playlistItemRequest = youTube.playlistItems().list("id,contentDetails,snippet")
        playlistItemRequest.playlistId = playLIstId
        // Only retrieve data used in this application, thereby making
        // the application more efficient. See:
        // https://developers.google.com/youtube/v3/getting-started#partial
        playlistItemRequest.fields =
            "items(contentDetails/videoId,snippet/title,snippet/publishedAt,snippet/thumbnails),nextPageToken,pageInfo"

        if (pageToken.isNotEmpty()) {
            playlistItemRequest.pageToken = pageToken
        }

        return playlistItemRequest.execute()
    }

    override fun getVideoDetail(youTube: YouTube, videoId: String): VideoListResponse {
        val videoRequest = youTube.videos().list("id,contentDetails")
        videoRequest.id = videoId
        return videoRequest.execute()
    }

    override suspend fun searchTrack(
        query: String,
        apiKey: String,
        pageToken: String
    ): YouTubeResponse {
        return if (pageToken.isEmpty()) {
            apiService.searchTrack(
                query, "video", apiKey, "snippet",
                20, "true", "true"
            )
        } else {
            apiService.searchTrack(
                query, "video", apiKey, "snippet",
                20, pageToken, "true", "true"
            )
        }
    }
}