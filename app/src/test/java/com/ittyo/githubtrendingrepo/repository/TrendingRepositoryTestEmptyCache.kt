package com.ittyo.githubtrendingrepo.repository

import com.ittyo.githubtrendingrepo.TestApp
import com.ittyo.githubtrendingrepo.createRepo
import com.ittyo.githubtrendingrepo.initializeClock
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
import org.threeten.bp.LocalDateTime
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [16], application = TestApp::class)
class TrendingRepositoryTestEmptyCache {

    @Mock
    lateinit var remote: RemoteDataStore

    @Mock
    lateinit var localCache: LocalDataStore

    lateinit var repository: GithubTrendingRepository

    private val cached = emptyList<Repo>()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        val currentTime = LocalDateTime.of(2020,2, 2,12, 0)
        val clock = initializeClock(currentTime)
        repository = GithubTrendingRepository(remote, localCache, clock)
        runBlocking {
            whenever(localCache.getTrendingRepo()).thenReturn(cached)
        }
    }

    @Test
    fun `should return result from remote - success case`() {
        runBlocking {
            val fetchResult = listOf(createRepo("ittyo"))
            whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

            val result = repository.getTrendingRepo(false)

            Assert.assertEquals(Result.Success(fetchResult), result)
        }
    }

    @Test
    fun `should return result from remote - error case`() {
        runBlocking {
            val fetchError = IOException("fetch failed")
            doAnswer { throw fetchError }.whenever(remote).fetchTrendingRepo()

            val result = repository.getTrendingRepo(false)

            Assert.assertEquals(Result.Failed(fetchError), result)
        }
    }

}