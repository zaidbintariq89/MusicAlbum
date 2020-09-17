package com.bell.youtubeplayer.models

import android.os.Parcelable
import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YouTubeItem(
    @SerializedName("etag")
    val eTag: String? = null,
    val snippet: YouTubeSnippet,
    val id: YoutubeId,
    var videoLength: String? = ""
) : Parcelable

fun createYoutubeItem(item: Playlist): YouTubeItem {
    val youtubeId = YoutubeId(videoId = item.id, kind = "list")
    val snippet = item.snippet

    val highImage = snippet.thumbnails.high
    val imagesModel =
        ImagesModel(height = highImage.height, width = highImage.width, url = highImage.url)
    val thumbnailModel = ThumbnailModel(defaultImage = imagesModel, highImage = imagesModel)

    val youtubeSnippet = YouTubeSnippet(
        channelId = snippet.channelId, title = snippet.title, channelTitle = snippet.channelTitle,
        description = snippet.description, thumbnails = thumbnailModel
    )

    return YouTubeItem(id = youtubeId, snippet = youtubeSnippet)
}

fun createYoutubeTrack(items: List<PlaylistItem>): ArrayList<YouTubeItem> {
    val playListItems: ArrayList<YouTubeItem> = ArrayList()

    for (item in items) {
        val snippet = item.snippet

        val content = item.contentDetails
        val youtubeId = YoutubeId(videoId = content.videoId, kind = "item")

        val youtubeSnippet = if(snippet.thumbnails != null && snippet.thumbnails.high != null) {
            val highImage = snippet.thumbnails.high
            val imagesModel =
                ImagesModel(height = highImage.height, width = highImage.width, url = highImage.url)
            val thumbnailModel = ThumbnailModel(defaultImage = imagesModel, highImage = imagesModel)

            YouTubeSnippet(title = snippet.title, thumbnails = thumbnailModel)
        } else {
            YouTubeSnippet(title = snippet.title)
        }

        playListItems.add(YouTubeItem(id = youtubeId, snippet = youtubeSnippet))
    }

    return playListItems
}