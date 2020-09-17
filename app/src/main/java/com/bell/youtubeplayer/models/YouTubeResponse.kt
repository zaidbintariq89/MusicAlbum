package com.bell.youtubeplayer.models

import java.util.*

data class YouTubeResponse(
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val items: ArrayList<YouTubeItem>? = null
)