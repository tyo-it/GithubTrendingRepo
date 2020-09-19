package com.ittyo.githubtrendingrepo

import com.ittyo.githubtrendingrepo.repository.Repo
import org.threeten.bp.Clock
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun createRepo(
    author: String = "author",
    name: String = "name",
    avatarUrl: String = "avatarUrl",
    repositoryUrl: String = "repositoryUrl",
    description: String = "description",
    language: String = "language",
    languageColor: String = "#000000",
    stars: Int = 0,
    forks: Int = 0
) = Repo(author, name, avatarUrl, repositoryUrl, description, language, languageColor, stars, forks)

fun initializeClock(currentTime: LocalDateTime): Clock {
    val zone = ZoneId.systemDefault()
    return Clock.fixed(currentTime.atZone(zone).toInstant(), zone)
}