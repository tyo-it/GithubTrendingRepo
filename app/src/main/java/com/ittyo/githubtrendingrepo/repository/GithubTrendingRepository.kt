package com.ittyo.githubtrendingrepo.repository

import android.util.Log

class GithubTrendingRepository(
    private val remote: RemoteDataStore,
    private val localDataStore: LocalDataStore
) {

    suspend fun getTrendingRepo(): Result {
        val cache = localDataStore.getTrendingRepo()
        val isCacheExpired = localDataStore.isTrendingRepoExpired()

        return if (cache.isEmpty() || isCacheExpired) {
            try {
                val result = remote.fetchTrendingRepo()
                localDataStore.saveTrendingRepo(result)
                Result.Success(result)
            } catch (e: Throwable) {
                Log.e("ERROR", e.message, e)
                Result.Failed(e)
            }
        } else {
            Result.Success(cache)
        }
    }
}