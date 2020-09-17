package com.bell.youtubeplayer.utils

import com.google.api.services.youtube.YouTubeScopes

object Constants {
    const val PREFS_NAME: String = "BELL_ANDROID_PREFS"
    const val PLAY_LIST_ITEM = "PLAY_LIST_ITEM"
    const val RC_SIGN_IN: Int = 9001
}

object NetworkConstants {
    const val YOUTUBE_SCOPE = YouTubeScopes.YOUTUBE_READONLY
    const val API_BASE_URL: String = "https://www.googleapis.com/youtube/v3/"

    const val SEARCH = "search"
    const val LOGIN_URL = "login"
}

enum class DialogAction(val action: String) {
    SUCCESS("success"),
    CANCEL("cancel")
}