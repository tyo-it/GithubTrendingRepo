package com.ittyo.githubtrendingrepo.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ittyo.githubtrendingrepo.*
import com.ittyo.githubtrendingrepo.repository.GithubTrendingRepository
import com.ittyo.githubtrendingrepo.repository.local.GithubTrendingLocalDataStore
import com.ittyo.githubtrendingrepo.repository.local.TrendingRepoDatabase
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingRemoteDataStore
import com.ittyo.githubtrendingrepo.repository.remote.GithubTrendingServiceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_placeholder_failed.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val EXPAND_POSITION_KEY = "expand_position"
    }

    private val viewModel by viewModels<TrendingRepoViewModel>()
    private val repoAdapter = TrendingRepoAdapter()
    private val loadingAdapter = LoadingRepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.stateLiveData.observe(this, Observer { state ->
            when (state) {
                is TrendingRepoViewModel.State.Loading -> {
                    showLoadingView()
                }

                is TrendingRepoViewModel.State.Success -> {
                    repoAdapter.setTrendingRepo(state.data)
                    showTrendingReposView()
                }

                is TrendingRepoViewModel.State.Failed -> {
                    showFailedLoadView()
                    retry_button.setOnClickListener {
                        viewModel.loadTrendingRepo()
                    }
                }
            }
        })

        setupAppbar()
        setupRepoRecyclerView()
        setupLoadingRecyclerView()
        viewModel.loadTrendingRepo()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXPAND_POSITION_KEY, repoAdapter.expandPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        repoAdapter.expandPosition = savedInstanceState.getInt(EXPAND_POSITION_KEY, -1)
    }

    private fun setupAppbar() {
        setSupportActionBar(toolbar)
        // we don't need actionbar title because we use textView to show title on the middle
        supportActionBar?.title = ""
    }

    private fun setupRepoRecyclerView() {
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        repo_recycler_view.layoutManager = LinearLayoutManager(this)
        repo_recycler_view.addItemDecoration(divider)
        repo_recycler_view.adapter = repoAdapter
        repo_recycler_view_container.setOnRefreshListener {
            viewModel.loadTrendingRepo(forceFetch = true)
        }
    }

    private fun setupLoadingRecyclerView() {
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        loading_recycler_view.layoutManager = LinearLayoutManager(this)
        loading_recycler_view.addItemDecoration(divider)
        loading_recycler_view.adapter = loadingAdapter
        loading_recycler_view.visibility = View.GONE
    }

    private fun showTrendingReposView() {
        repo_recycler_view_container.isRefreshing = false
        repo_recycler_view_container.visibility = View.VISIBLE
        loading_recycler_view.visibility = View.GONE
        repo_recycler_view.visibility = View.VISIBLE
        failed_view.visibility = View.GONE
    }

    private fun showLoadingView() {
        repo_recycler_view_container.visibility = View.VISIBLE
        loading_recycler_view.visibility = View.VISIBLE
        repo_recycler_view.visibility = View.INVISIBLE
        failed_view.visibility = View.GONE
    }

    private fun showFailedLoadView() {
        failed_view.visibility = View.VISIBLE
        loading_recycler_view.visibility = View.GONE
        repo_recycler_view_container.visibility = View.GONE
        repo_recycler_view_container.isRefreshing = false
    }
}