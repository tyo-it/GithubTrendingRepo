package com.ittyo.githubtrendingrepo.repository

class GithubTrendingRepository(
    private val remote: RemoteDataStore,
    private val localDataStore: LocalDataStore
) {

    suspend fun getTrendingRepo(forceFetch: Boolean): Result {
        val cache = localDataStore.getTrendingRepo()
        val isCacheExpired = localDataStore.isTrendingRepoExpired()

        return if (cache.isEmpty() || isCacheExpired || forceFetch) {
            try {
                val result = remote.fetchTrendingRepo()
                localDataStore.saveTrendingRepo(result)
                Result.Success(result)
            } catch (e: Throwable) {
                Result.Failed(e)
            }
        } else {
            Result.Success(cache)
        }
    }
}