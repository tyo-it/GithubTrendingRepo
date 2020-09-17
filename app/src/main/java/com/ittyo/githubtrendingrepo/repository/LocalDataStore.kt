package com.ittyo.githubtrendingrepo.repository

interface LocalDataStore {

    fun getTrendingRepo(): List<Repo>
    fun isTrendingRepoExpired(): Boolean
    fun saveTrendingRepo(repos: List<Repo>)
}