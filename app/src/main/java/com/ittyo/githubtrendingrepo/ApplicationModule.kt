package com.ittyo.githubtrendingrepo

import android.content.Context
import android.content.SharedPreferences
import com.ittyo.githubtrendingrepo.repository.local.TrendingRepoDatabase
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingService
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideGithubTrendingService(@ApplicationContext appContext: Context): GithubTrendingService {
        val baseUrl = (appContext as TrendingRepoApp).getBaseUrl()
        return GithubTrendingServiceFactory.makeGithubService(BuildConfig.DEBUG, baseUrl)
    }

    @Singleton
    @Provides
    fun provideTrendingRepoDatabase(@ApplicationContext appContext: Context): TrendingRepoDatabase {
        return TrendingRepoDatabase.getInstance(appContext)
    }

    @Singleton
    @Provides
    @RepoCache
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("repo", Context.MODE_PRIVATE)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepoCache