package com.ittyo.githubtrendingrepo.repository

import com.ittyo.githubtrendingrepo.createRepo
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

class GithubTrendingRepositoryTest {

    @Mock
    lateinit var remote: RemoteDataStore

    @Mock
    lateinit var localCache: LocalDataStore

    lateinit var repository: GithubTrendingRepository

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        repository = GithubTrendingRepository(remote, localCache)
    }

    @Test
    fun `given local cache is empty then should return result from remote`() {
        val fetchResult = listOf(createRepo("ittyo"))
        whenever(localCache.getTrendingRepo()).thenReturn(emptyList())
        whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

        val result = repository.getTrendingRepo()

        assertEquals(Result.Success(fetchResult), result)
    }

    @Test
    fun `given local cache is not empty but expired then should return result from remote`() {
        val fetchResult = listOf(createRepo("ittyo"))
        whenever(localCache.getTrendingRepo()).thenReturn(listOf(createRepo()))
        whenever(localCache.isTrendingRepoExpired()).thenReturn(true)
        whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

        val result = repository.getTrendingRepo()

        assertEquals(Result.Success(fetchResult), result)
    }

    @Test
    fun `given local cache is not empty and not expired then should return result from cache`() {
        val caches = listOf(createRepo("ittyo"))
        whenever(localCache.getTrendingRepo()).thenReturn(caches)
        whenever(localCache.isTrendingRepoExpired()).thenReturn(false)

        val result = repository.getTrendingRepo()

        assertEquals(Result.Success(caches), result)
    }

    @Test
    fun `when fetch from remote throw exception should return Failed`() {
        val fetchError = IOException("fetch failed")
        whenever(localCache.getTrendingRepo()).thenReturn(emptyList())
        doAnswer { throw fetchError }.whenever(remote).fetchTrendingRepo()

        val result = repository.getTrendingRepo()

        assertEquals(Result.Failed(fetchError), result)
    }

    @Test
    fun `when fetch from remote successfully should save to localCache`() {
        val fetchResult = listOf(createRepo("ittyo"))
        whenever(localCache.getTrendingRepo()).thenReturn(emptyList())
        whenever(remote.fetchTrendingRepo()).thenReturn(fetchResult)

        repository.getTrendingRepo()

        verify(localCache).saveTrendingRepo(fetchResult)
    }
}