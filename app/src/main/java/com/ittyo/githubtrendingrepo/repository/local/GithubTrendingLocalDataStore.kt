package com.ittyo.githubtrendingrepo.repository.local

import android.content.SharedPreferences
import com.ittyo.githubtrendingrepo.repository.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.Repo
import java.util.*

const val LAST_UPDATE_TIMESTAMP_KEY = "LAST_UPDATE_TIMESTAMP_KEY"

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
        val timeStamp = sharedPref.getLong(LAST_UPDATE_TIMESTAMP_KEY, 0)
        val currentTime = Date().time
        return inHour(currentTime - timeStamp) > 2.0
    }

    private fun inHour(milis: Long): Double {
        val hour = (1000*60*60).toDouble()
        return milis/hour
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
            val currentTime = Date().time
            this.putLong(LAST_UPDATE_TIMESTAMP_KEY, currentTime)
            commit()
        }
    }
}