package com.ittyo.githubtrendingrepo.repository

import com.ittyo.githubtrendingrepo.TestApp
import com.ittyo.githubtrendingrepo.createRepo
import com.ittyo.githubtrendingrepo.initializeClock
import com.ittyo.githubtrendingrepo.repository.local.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.remote.RemoteDataStore
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.threeten.bp.*
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [16], application = TestApp::class)
class TrendingRepositoryTestNotEmptyCache {

    @Mock
    lateinit var remote: RemoteDataStore

    @Mock
    lateinit var localCache: LocalDataStore

    private var currentTime = LocalDateTime.of(2020, 2, 2, 12, 0)

    lateinit var repository: GithubTrendingRepository

    private val cached = listOf(createRepo("cached"))

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        val clock = initializeClock(currentTime)
        repository = GithubTrendingRepository(remote, localCache, clock)
        runBlocking {
            whenever(localCache.getTrendingRepo()).thenReturn(cached)
        }
    }

    @Test
    fun `given local cache is expired then should return result from remote - success case`() {
        runBlocking {
            val fetchResult = listOf(createRepo("ittyo"))
            val lastUpdate = currentTime.minusMinutes(121)
            whenever(localCache.getTrendingRepo()).thenReturn(listOf(createRepo()))
            whenever(localCache.getTrendingRepoLastUpdate()).thenReturn(lastUpdate)
            whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

            val result = repository.getTrendingRepo(false)

            Assert.assertEquals(Result.Success(fetchResult), result)
        }
    }

    @Test
    fun `given local cache is expired then should return result from remote - error case`() {
        runBlocking {
            val fetchError = IOException("fetch failed")
            val lastUpdate = currentTime.minusMinutes(121)
            whenever(localCache.getTrendingRepoLastUpdate()).thenReturn(lastUpdate)
            doAnswer { throw fetchError }.whenever(remote).fetchTrendingRepo()

            val result = repository.getTrendingRepo(false)

            Assert.assertEquals(Result.Failed(fetchError), result)
        }
    }

    @Test
    fun `given local cache is not expired then should return result from cache`() {
        runBlocking {
            val caches = listOf(createRepo("ittyo"))
            val lastUpdate = currentTime.minusMinutes(120)
            whenever(localCache.getTrendingRepo()).thenReturn(caches)
            whenever(localCache.getTrendingRepoLastUpdate()).thenReturn(lastUpdate)

            val result = repository.getTrendingRepo(false)

            Assert.assertEquals(Result.Success(caches), result)
        }
    }
}