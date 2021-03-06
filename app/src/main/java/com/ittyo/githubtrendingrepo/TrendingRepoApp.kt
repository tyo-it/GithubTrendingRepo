package com.ittyo.githubtrendingrepo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class TrendingRepoApp: Application() {

    open fun getBaseUrl() = "https://ghapi.huchen.dev/"

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}