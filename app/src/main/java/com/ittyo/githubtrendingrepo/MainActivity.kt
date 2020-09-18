package com.ittyo.githubtrendingrepo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ittyo.githubtrendingrepo.repository.GithubTrendingRepository
import com.ittyo.githubtrendingrepo.repository.local.GithubTrendingLocalDataStore
import com.ittyo.githubtrendingrepo.repository.local.TrendingRepoDatabase
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingRemoteDataStore
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingServiceFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_placeholder_failed.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TrendingRepoViewModel

    private val repoAdapter = TrendingRepoAdapter()
    private val loadingAdapter = LoadingRepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(TrendingRepoViewModel::class.java)

        viewModel.stateLiveData.observe(this, Observer { state ->
            when (state) {
                is TrendingRepoViewModel.State.Loading -> {
                    repo_recycler_view.visibility = View.GONE
                    loading_recycler_view.visibility = View.VISIBLE
                    failed_view.visibility = View.GONE
                }

                is TrendingRepoViewModel.State.Success -> {
                    repoAdapter.setTrendingRepo(state.data)
                    repo_recycler_view.visibility = View.VISIBLE
                    loading_recycler_view.visibility = View.GONE
                    failed_view.visibility = View.GONE
                }

                is TrendingRepoViewModel.State.Failed -> {
                    failed_view.visibility = View.VISIBLE
                    loading_recycler_view.visibility = View.GONE
                    repo_recycler_view.visibility = View.GONE
                    retry_button.setOnClickListener {
                        viewModel.loadTrendingRepo()
                    }
                }
            }
        })

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        repo_recycler_view.layoutManager = LinearLayoutManager(this)
        repo_recycler_view.addItemDecoration(divider)
        repo_recycler_view.adapter = repoAdapter

        loading_recycler_view.layoutManager = LinearLayoutManager(this)
        loading_recycler_view.addItemDecoration(divider)
        loading_recycler_view.adapter = loadingAdapter
        loading_recycler_view.visibility = View.GONE

        viewModel.loadTrendingRepo()
    }

    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TrendingRepoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val githubService = GithubTrendingServiceFactory.makeGithubService(true)
                val remote = GithubTrendingRemoteDataStore(githubService)

                val database = TrendingRepoDatabase.getInstance(context)
                val sharedPref = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
                val localDataStore = GithubTrendingLocalDataStore(database, sharedPref)
                val repository = GithubTrendingRepository(remote, localDataStore)
                return TrendingRepoViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}