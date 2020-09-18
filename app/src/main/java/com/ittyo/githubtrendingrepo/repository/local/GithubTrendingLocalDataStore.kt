package com.ittyo.githubtrendingrepo.repository.local

import com.ittyo.githubtrendingrepo.repository.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.Repo

class GithubTrendingLocalDataStore(private val database: TrendingRepoDatabase): LocalDataStore {

    override suspend fun getTrendingRepo(): List<Repo> {
        return database.trendingRepoDao().getTrendingRepo().map {
            Repo(
                author = it.author.orEmpty(),
                name = it.name.orEmpty(),
                avatarUrl = it.avatar.orEmpty(),
                repositoryUrl = it.url.orEmpty(),
                description = it.description.orEmpty(),
                language = it.language.orEmpty(),
                stars = it.stars,
                forks = it.forks
            )
        }
    }

    override suspend fun isTrendingRepoExpired(): Boolean {
        return true
    }

    override suspend fun saveTrendingRepo(repos: List<Repo>) {
        val entities = repos.map {
            TrendingRepoEntity(
                id = 0,
                author = it.author,
                name = it.name,
                avatar = it.avatarUrl,
                url = it.repositoryUrl,
                description = it.description,
                language = it.language,
                stars = it.stars,
                forks = it.forks
            )
        }
        database.trendingRepoDao().insertAll(entities)
    }
}