package com.ittyo.githubtrendingrepo.repository

interface TrendingRepository {

    suspend fun getTrendingRepo(forceFetch: Boolean): Result
}