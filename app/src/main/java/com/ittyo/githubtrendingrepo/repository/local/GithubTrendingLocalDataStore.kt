package com.ittyo.githubtrendingrepo.repository.local

import android.content.SharedPreferences
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
                languageColor = it.languageColor.orEmpty(),
                stars = it.stars,
                forks = it.forks
            )
        }
    }

    override suspend fun saveTrendingRepo(repos: List<Repo>, currentTime: LocalDateTime) {
        val entities = repos.map {
            TrendingRepoEntity(
                id = 0,
                author = it.author,
                name = it.name,
                avatar = it.avatarUrl,
                url = it.repositoryUrl,
                description = it.description,
                language = it.language,
                languageColor = it.languageColor,
                stars = it.stars,
                forks = it.forks
            )
        }
        database.trendingRepoDao().insertAll(entities)
        setTrendingRepoLastUpdate(currentTime)
    }

    override suspend fun clearTrendingRepo() {
        database.trendingRepoDao().deleteAll()
    }

    override fun getTrendingRepoLastUpdate(): LocalDateTime {
        val timeString = sharedPref.getString(LAST_UPDATE_TIME_KEY, "1970-01-01T07:00")
        return LocalDateTime.parse(timeString)
    }

    private fun setTrendingRepoLastUpdate(currentTime: LocalDateTime) {
        with (sharedPref.edit()) {
            this.putString(LAST_UPDATE_TIME_KEY, currentTime.toString())
            commit()
        }
    }
}