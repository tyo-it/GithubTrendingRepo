package com.ittyo.githubtrendingrepo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class TestApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}