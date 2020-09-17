package com.bell.youtubeplayer

import com.bell.youtubeplayer.models.YouTubeResponse
import com.bell.youtubeplayer.repository.INetworkRepository
import com.bell.youtubeplayer.repository.NetworkRepository
import com.bell.youtubeplayer.repository.core.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @Mock
    private lateinit var apiService: APIService
    private val apiKey = "KEY"
    private lateinit var networkRepository: INetworkRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkRepository = NetworkRepository(apiService)
    }

    @Test
    fun testNoResultsReturned_whenNoQueryEnter() = runBlocking(Dispatchers.Default) {
        Assert.assertEquals(null,networkRepository.searchTrack("",apiKey,""))
    }

    @Test
    fun whenRequested_fetchResults() {
        GlobalScope.launch(Dispatchers.Default) {
            val youtubeResponse = YouTubeResponse()
            Assert.assertEquals(youtubeResponse,networkRepository.searchTrack("android",apiKey,""))
        }
    }
}