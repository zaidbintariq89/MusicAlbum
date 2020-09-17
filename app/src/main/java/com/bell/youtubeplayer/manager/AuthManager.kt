package com.bell.youtubeplayer.manager

import android.content.Context
import com.bell.youtubeplayer.utils.NetworkConstants
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube

object AuthManager {

    var googleSignInAccount: GoogleSignInAccount? = null
    var mGoogleSignInClient: GoogleSignInClient? = null

    fun getYoutubeInstance(context: Context): YouTube {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, setOf(NetworkConstants.YOUTUBE_SCOPE)
        )
        credential.selectedAccount = googleSignInAccount!!.account
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

        return YouTube.Builder(transport, jsonFactory, credential)
            .setApplicationName("BellMusicPlayer")
            .build()
    }
}