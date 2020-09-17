package com.ittyo.githubtrendingrepo.repository

interface RemoteDataStore {

    suspend fun fetchTrendingRepo(): List<Repo>
}