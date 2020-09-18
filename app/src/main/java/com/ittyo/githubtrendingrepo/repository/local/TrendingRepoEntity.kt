package com.ittyo.githubtrendingrepo.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo")
data class TrendingRepoEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "avatar")
    val avatar: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "language")
    val language: String?,

    @ColumnInfo(name = "stars")
    val stars: Int,

    @ColumnInfo(name = "forks")
    val forks: Int
)