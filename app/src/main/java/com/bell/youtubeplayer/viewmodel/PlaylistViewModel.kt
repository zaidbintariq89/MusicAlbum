package com.bell.youtubeplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bell.youtubeplayer.BellApplication
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PlaylistViewModel : BaseViewModel() {

    private val networkRepository = BellApplication.getNetworkRepository()
    val playlistObservable: MutableLiveData<List<Playlist>?> = MutableLiveData()
    val playlistItems: ArrayList<Playlist> = ArrayList()

    fun getPlayList(youTube: YouTube) {
        viewModelScope.launch(Dispatchers.Default) {
            playlistItems.clear()
            fetchPlayList(youTube, "")
        }
    }

    private fun fetchPlayList(youTube: YouTube, pageToken: String) {
        try {
            val response = networkRepository?.getPlayList(youTube, pageToken)
            if (response != null) {
                val playlistList = response.items
                if (playlistList != null && playlistList.isNotEmpty()) {
                    playlistItems.addAll(playlistList)
                }

                // fetch next items
                if (!response.nextPageToken.isNullOrEmpty()) {
                    fetchPlayList(youTube, response.nextPageToken)
                } else {
                    // post to UI
                    playlistObservable.postValue(playlistItems)
                }

            } else {
                playlistObservable.postValue(null)
            }

        } catch (e: Exception) {
            playlistObservable.postValue(null)
        }
    }
}