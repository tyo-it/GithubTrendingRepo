package com.ittyo.githubtrendingrepo.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.ittyo.githubtrendingrepo.TrendingRepoTestApp

class MockTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?,
                                className: String?,
                                context: Context?): Application {
        return super.newApplication(cl, TrendingRepoTestApp::class.java.name, context)
    }
}