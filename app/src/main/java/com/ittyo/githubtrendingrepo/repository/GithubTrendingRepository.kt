package com.ittyo.githubtrendingrepo.repository

import com.ittyo.githubtrendingrepo.repository.local.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.remote.RemoteDataStore
import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class GithubTrendingRepository @Inject constructor(
    private val remote: RemoteDataStore,
    private val localDataStore: LocalDataStore,
    private val clock: Clock
): TrendingRepository {

    override suspend fun getTrendingRepo(forceFetch: Boolean): Result {
        val cache = localDataStore.getTrendingRepo()

        return if (forceFetch || cache.isEmpty() || isCacheExpired()) {
            try {
                val result = remote.fetchTrendingRepo()
                val currentTime = LocalDateTime.now(clock)
                localDataStore.clearTrendingRepo()
                localDataStore.saveTrendingRepo(result, currentTime)
                Result.Success(result)
            } catch (e: Throwable) {
                Result.Failed(e)
            }
        } else {
            Result.Success(cache)
        }
    }

    private fun isCacheExpired(): Boolean {
        val lastUpdate = localDataStore.getTrendingRepoLastUpdate()
        val currentTime = LocalDateTime.now(clock)
        return Duration.between(lastUpdate, currentTime).toMinutes() > 120
    }
}