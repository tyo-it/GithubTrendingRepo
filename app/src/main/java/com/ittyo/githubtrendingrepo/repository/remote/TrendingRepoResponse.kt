package com.ittyo.githubtrendingrepo.repository.remote

import com.google.gson.annotations.SerializedName

data class TrendingRepoResponse(
    @SerializedName("author")
    val author: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("language")
    val language: String?,

    @SerializedName("stars")
    val stars: Int,

    @SerializedName("forks")
    val forks: Int
)