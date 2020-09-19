package com.ittyo.githubtrendingrepo.repository

import org.threeten.bp.LocalDateTime

interface LocalDataStore {

    suspend fun getTrendingRepo(): List<Repo>
    suspend fun saveTrendingRepo(repos: List<Repo>, currentTime: LocalDateTime)
    suspend fun clearTrendingRepo()
    fun getTrendingRepoLastUpdate(): LocalDateTime
}