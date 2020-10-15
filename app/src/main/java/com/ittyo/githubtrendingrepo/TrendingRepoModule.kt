package com.ittyo.githubtrendingrepo

import com.ittyo.githubtrendingrepo.repository.GithubTrendingRepository
import com.ittyo.githubtrendingrepo.repository.TrendingRepository
import com.ittyo.githubtrendingrepo.repository.local.GithubTrendingLocalDataStore
import com.ittyo.githubtrendingrepo.repository.local.LocalDataStore
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingRemoteDataStore
import com.ittyo.githubtrendingrepo.repository.remote.RemoteDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import org.threeten.bp.Clock

@Module
@InstallIn(ActivityComponent::class)
abstract class BindTrendingRepoModule {

    @Binds
    abstract fun bindTrendingRepository(githubTrendingRepository: GithubTrendingRepository): TrendingRepository

    @Binds
    abstract fun bindRemoteDataStore(githubTrendingRemoteDataStore: GithubTrendingRemoteDataStore): RemoteDataStore

    @Binds
    abstract fun bindLocalDataStore(localDataStore: GithubTrendingLocalDataStore): LocalDataStore
}

@Module
@InstallIn(ActivityComponent::class)
object ProvideTrendingRepoModule {

    @Provides
    fun provideClock(): Clock {
        return Clock.systemDefaultZone()
    }
}