package com.ittyo.githubtrendingrepo.repository.local

import androidx.room.*

@Dao
interface TrendingRepoDao {

    @Query("SELECT * FROM repo")
    suspend fun getTrendingRepo(): List<TrendingRepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<TrendingRepoEntity>)

    @Query("DELETE FROM repo")
    suspend fun deleteAll()
}