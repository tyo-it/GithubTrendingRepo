package com.ittyo.githubtrendingrepo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ittyo.githubtrendingrepo.repository.TrendingRepoAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val repoAdapter = TrendingRepoAdapter()
    private val loadingAdapter = LoadingRepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repo_recycler_view.layoutManager = LinearLayoutManager(this)
        repo_recycler_view.adapter = repoAdapter
        repo_recycler_view.visibility = View.GONE

        loading_recycler_view.layoutManager = LinearLayoutManager(this)
        loading_recycler_view.adapter = loadingAdapter
    }
}