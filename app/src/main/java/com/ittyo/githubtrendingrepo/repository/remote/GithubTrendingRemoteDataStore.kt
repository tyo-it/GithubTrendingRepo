package com.ittyo.githubtrendingrepo.repository.remote

import com.ittyo.githubtrendingrepo.repository.RemoteDataStore
import com.ittyo.githubtrendingrepo.repository.Repo

class GithubTrendingRemoteDataStore(private val service: GithubTrendingService): RemoteDataStore {
    override suspend fun fetchTrendingRepo(): List<Repo> {
        return service.getTrendingRepo()
    }
}