package com.ittyo.githubtrendingrepo.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrendingRepoEntity::class], version = 1)
abstract class TrendingRepoDatabase: RoomDatabase() {
    abstract fun trendingRepoDao(): TrendingRepoDao

    companion object {

        @Volatile
        private var INSTANCE: TrendingRepoDatabase? = null

        fun getInstance(context: Context): TrendingRepoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext, TrendingRepoDatabase::class.java, "TrendingRepo.db")
                .build()
    }
}