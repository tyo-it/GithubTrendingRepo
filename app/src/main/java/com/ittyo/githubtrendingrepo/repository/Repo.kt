package com.ittyo.githubtrendingrepo.repository

data class Repo (
    val author: String,
    val name: String,
    val avatarUrl: String,
    val repositoryUrl: String,
    val description: String,
    val language: String,
    val stars: Int,
    val forks: Int
)