package com.ittyo.githubtrendingrepo.repository

interface RemoteDataStore {

    fun fetchTrendingRepo(): List<Repo>
}