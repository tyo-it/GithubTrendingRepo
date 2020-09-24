package com.ittyo.githubtrendingrepo.repository

import com.ittyo.githubtrendingrepo.TestApp
import com.ittyo.githubtrendingrepo.createRepo
import com.ittyo.githubtrendingrepo.initializeClock
import com.ittyo.githubtrendingrepo.repository.local.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.remote.RemoteDataStore
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.threeten.bp.LocalDateTime
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [16], application = TestApp::class)
class TrendingRepositoryTestForceFetch {

    @Mock
    lateinit var remote: RemoteDataStore

    @Mock
    lateinit var localCache: LocalDataStore

    private val currentTime = LocalDateTime.of(2020, 2, 2, 12, 0)

    private lateinit var repository: GithubTrendingRepository

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        val clock = initializeClock(currentTime)
        repository = GithubTrendingRepository(remote, localCache, clock)
        runBlocking {
            whenever(localCache.getTrendingRepo()).thenReturn(emptyList())
        }
    }

    @Test
    fun `should fetch from remote - success case`() {
        runBlocking {
            val fetchResult = listOf(createRepo("ittyo"))
            whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

            val result = repository.getTrendingRepo(true)

            assertEquals(Result.Success(fetchResult), result)
        }
    }

    @Test
    fun `should fetch from remote - failed case`() {
        runBlocking {
            val fetchError = IOException("fetch failed")
            doAnswer { throw fetchError }.whenever(remote).fetchTrendingRepo()

            val result = repository.getTrendingRepo(true)

            assertEquals(Result.Failed(fetchError), result)
        }
    }

    @Test
    fun `should clear the cache before save the success result`() {
        runBlocking {
            val fetchResult = listOf(createRepo("ittyo"))
            whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

            repository.getTrendingRepo(true)

            inOrder(localCache)
            verify(localCache, times(1)).clearTrendingRepo()
            verify(localCache, times(1)).saveTrendingRepo(fetchResult, currentTime)
        }
    }
}