package com.ittyo.githubtrendingrepo

import android.app.Application

open class TrendingRepoApp: Application() {

    open fun getBaseUrl() = "https://ghapi.huchen.dev/"
}