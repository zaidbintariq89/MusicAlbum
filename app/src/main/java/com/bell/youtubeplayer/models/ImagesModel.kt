package com.bell.youtubeplayer.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesModel(
    val height: Long,
    val width: Long,
    val url: String
) : Parcelable