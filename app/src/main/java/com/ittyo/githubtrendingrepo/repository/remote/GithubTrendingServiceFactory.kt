package com.ittyo.githubtrendingrepo.repository.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GithubTrendingServiceFactory {

    fun makeGithubService(isDebug: Boolean, baseUrl: String): GithubTrendingService {
        val okHttpClient =
            makeOkHttpClient(
                makeLoggingInterceptor(
                    (isDebug)
                )
            )
        return makeGithubService(
            okHttpClient,
            Gson(),
            baseUrl
        )
    }

    private fun makeGithubService(okHttpClient: OkHttpClient, gson: Gson, baseUrl: String): GithubTrendingService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(GithubTrendingService::class.java)
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}