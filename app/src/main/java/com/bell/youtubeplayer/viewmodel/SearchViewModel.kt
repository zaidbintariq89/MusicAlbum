package com.bell.youtubeplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bell.youtubeplayer.BellApplication
import com.bell.youtubeplayer.models.YouTubeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {

    private val networkRepository = BellApplication.getNetworkRepository()
    private val searchItems: ArrayList<YouTubeItem> = ArrayList()
    val searchObservable: MutableLiveData<ArrayList<YouTubeItem>?> = MutableLiveData()
    var nextPageToken : String = ""

    fun searchTrack(query: String, apiKey: String, pageToken: String) {
        if (pageToken.isEmpty()) {
            searchItems.clear()
        }
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = networkRepository?.searchTrack(query,apiKey , pageToken)
                if (response != null) {
                    val items = response.items
                    if (!items.isNullOrEmpty()) {
                        searchItems.addAll(items)
                    }
                    if (!response.nextPageToken.isNullOrEmpty()) {
                        nextPageToken = response.nextPageToken
                    }

                    searchObservable.postValue(searchItems)
                }
            } catch (e: Exception) {
                searchObservable.postValue(null)
            }
        }

    }

}