package com.bell.youtubeplayer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YouTubeSnippet(
    val channelId: String? = null,
    val title: String,
    val description: String? = null,
    val channelTitle: String? = null,
    val thumbnails: ThumbnailModel? = null
) : Parcelable

@Parcelize
data class ThumbnailModel(
    @SerializedName("default")
    val defaultImage: ImagesModel? = null,
    @SerializedName("medium")
    val mediumImage: ImagesModel? = null,
    @SerializedName("high")
    val highImage: ImagesModel? = null
) : Parcelable