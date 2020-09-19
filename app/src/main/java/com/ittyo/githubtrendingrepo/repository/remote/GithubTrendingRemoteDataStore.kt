package com.ittyo.githubtrendingrepo.repository.remote

import com.ittyo.githubtrendingrepo.repository.RemoteDataStore
import com.ittyo.githubtrendingrepo.repository.Repo

class GithubTrendingRemoteDataStore(private val service: GithubTrendingService): RemoteDataStore {
    override suspend fun fetchTrendingRepo(): List<Repo> {
        return service.getTrendingRepo().map {
            Repo(
                author = it.author.orEmpty(),
                name = it.name.orEmpty(),
                avatarUrl = it.avatar.orEmpty(),
                repositoryUrl = it.url.orEmpty(),
                description = it.description.orEmpty(),
                language = it.language.orEmpty(),
                languageColor = it.languageColor.orEmpty(),
                stars = it.stars,
                forks = it.forks
            )
        }
    }
}