package com.ittyo.githubtrendingrepo.repository

interface LocalDataStore {

    suspend fun getTrendingRepo(): List<Repo>
    suspend fun isTrendingRepoExpired(): Boolean
    suspend fun saveTrendingRepo(repos: List<Repo>)
}