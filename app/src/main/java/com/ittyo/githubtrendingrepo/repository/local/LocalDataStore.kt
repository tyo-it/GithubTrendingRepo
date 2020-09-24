package com.ittyo.githubtrendingrepo.repository.local

import com.ittyo.githubtrendingrepo.repository.Repo
import org.threeten.bp.LocalDateTime

interface LocalDataStore {

    suspend fun getTrendingRepo(): List<Repo>
    suspend fun saveTrendingRepo(repos: List<Repo>, currentTime: LocalDateTime)
    suspend fun clearTrendingRepo()
    fun getTrendingRepoLastUpdate(): LocalDateTime
}