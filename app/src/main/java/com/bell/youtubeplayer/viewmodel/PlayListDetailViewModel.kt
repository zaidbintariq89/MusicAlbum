package com.bell.youtubeplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bell.youtubeplayer.BellApplication
import com.bell.youtubeplayer.models.YouTubeItem
import com.bell.youtubeplayer.models.createYoutubeTrack
import com.bell.youtubeplayer.utils.TimeUtils
import com.google.api.services.youtube.YouTube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListDetailViewModel : BaseViewModel() {

    private val networkRepository = BellApplication.getNetworkRepository()
    private val playListItems: ArrayList<YouTubeItem> = ArrayList()
    val playlistObservable: MutableLiveData<ArrayList<YouTubeItem>?> = MutableLiveData()

    fun getPlayListDetails(youTube: YouTube, playListId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            playListItems.clear()
            try {
                fetchPlayListItems(youTube, playListId, "")
            } catch (e: Exception) {
                playlistObservable.postValue(null)
            }
        }
    }

    private fun fetchPlayListItems(youTube: YouTube, playListId: String, pageToken: String) {
        val response = networkRepository?.getPlayListDetails(youTube, playListId, pageToken)
        if (response != null) {
            val items = response.items
            if (items != null && items.isNotEmpty()) {
                val youtubeItem = createYoutubeTrack(items)
                playListItems.addAll(youtubeItem)
            }

            if (!response.nextPageToken.isNullOrEmpty()) {
                fetchPlayListItems(youTube, playListId,response.nextPageToken)
            } else {
                // post to UI
                playListItems.forEach {
                    val length = getVideoDetail(youTube, it.id.videoId)
                    if (length.isNotEmpty()) {
                        it.videoLength = length
                    }
                }
                playlistObservable.postValue(playListItems)
            }
        } else {
            playlistObservable.postValue(null)
        }
    }

    private fun getVideoDetail(youTube: YouTube, videoId: String): String {
        var length = ""
        val response = networkRepository?.getVideoDetail(youTube, videoId)
        if (response != null) {
            val items = response.items
            if (items != null && !items.isNullOrEmpty()) {
                val content = items[0].contentDetails
                if (content != null) {
                    length = TimeUtils.parseVideoTime(content.duration)
                }
            }
        }

        return length
    }
}