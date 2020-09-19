package com.ittyo.githubtrendingrepo.repository.local

import android.content.SharedPreferences
import com.ittyo.githubtrendingrepo.repository.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.Repo
import org.threeten.bp.*

const val LAST_UPDATE_TIME_KEY = "LAST_UPDATE_TIMESTAMP_KEY"

class GithubTrendingLocalDataStore(private val database: TrendingRepoDatabase,
                                   private val sharedPref: SharedPreferences): LocalDataStore {

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
        val timeStamp = sharedPref.getString(LAST_UPDATE_TIME_KEY, "1970-01-01T07:00")
        val lastUpdateTime = LocalDateTime.parse(timeStamp)
        val currentTime = LocalDateTime.now()
        return Duration.between(lastUpdateTime, currentTime).toMinutes() > 120
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

        with(sharedPref.edit()) {
            val currentTime = LocalDateTime.now()
            this.putString(LAST_UPDATE_TIME_KEY, currentTime.toString())
            commit()
        }
    }
}