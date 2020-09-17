package com.ittyo.githubtrendingrepo

import com.ittyo.githubtrendingrepo.repository.Repo

fun createRepo(
    author: String = "author",
    name: String = "name",
    avatarUrl: String = "avatarUrl",
    description: String = "description",
    language: String = "language",
    stars: Int = 0,
    forks: Int = 0
) = Repo(author, name, avatarUrl, description, language, stars, forks)