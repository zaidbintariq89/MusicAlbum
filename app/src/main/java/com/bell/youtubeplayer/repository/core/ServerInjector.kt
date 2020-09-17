package com.bell.youtubeplayer.repository.core

import com.bell.youtubeplayer.BuildConfig
import com.bell.youtubeplayer.utils.NetworkConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServerInjector {

    private var retrofit: Retrofit? = null

    private fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        return builder.build()
    }

    private fun provideRetrofit(): Retrofit {
        val httpClient = getOkHttpClient()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    fun provideApiService(): APIService {
        if (retrofit == null) {
            retrofit = provideRetrofit()
        }
        return retrofit!!.create(APIService::class.java)
    }
}