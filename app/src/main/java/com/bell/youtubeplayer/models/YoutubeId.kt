package com.bell.youtubeplayer.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubeId(
    val videoId: String,
    val kind: String
) : Parcelable