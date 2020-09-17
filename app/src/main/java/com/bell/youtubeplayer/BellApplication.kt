package com.bell.youtubeplayer

import android.app.Application
import com.bell.youtubeplayer.repository.INetworkRepository
import com.bell.youtubeplayer.repository.NetworkRepository
import com.bell.youtubeplayer.repository.core.ServerInjector

class BellApplication : Application() {

    companion object {
        private lateinit var instance: BellApplication
        private val apiService = ServerInjector.provideApiService()
        private var networkRepository: INetworkRepository? = null

        fun getApplication() = instance

        // Dagger can be used here for dependency Injection
        fun getNetworkRepository(): INetworkRepository? {
            if (networkRepository == null) {
                networkRepository = NetworkRepository(apiService)
            }
            return networkRepository
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}