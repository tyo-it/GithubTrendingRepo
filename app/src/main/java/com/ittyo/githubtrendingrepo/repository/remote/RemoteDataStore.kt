package com.ittyo.githubtrendingrepo.repository.remote

import com.ittyo.githubtrendingrepo.repository.Repo

interface RemoteDataStore {

    suspend fun fetchTrendingRepo(): List<Repo>
}