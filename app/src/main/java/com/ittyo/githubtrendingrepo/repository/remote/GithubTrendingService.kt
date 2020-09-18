package com.ittyo.githubtrendingrepo.repository.remote

import com.ittyo.githubtrendingrepo.repository.Repo
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubTrendingService {
    @GET("repositories")
    suspend fun getTrendingRepo (
        @Query("language") language: String = "",
        @Query("since") since: String = "daily",
        @Query("spoken_language_code") spokenLanguageCode: String = ""
    ): List<TrendingRepoResponse>
}