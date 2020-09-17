package com.ittyo.githubtrendingrepo.repository

sealed class Result {
    data class Success(val data: List<Repo>): Result()
    data class Failed(val error: Throwable): Result()
}