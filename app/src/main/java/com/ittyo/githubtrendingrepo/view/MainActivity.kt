package com.ittyo.githubtrendingrepo.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_placeholder_failed.*
import org.threeten.bp.Clock

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXPAND_POSITION_KEY = "expand_position"
    }

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

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        repo_recycler_view.layoutManager = LinearLayoutManager(this)
        repo_recycler_view.addItemDecoration(divider)
        repo_recycler_view.adapter = repoAdapter

        loading_recycler_view.layoutManager = LinearLayoutManager(this)
        loading_recycler_view.addItemDecoration(divider)
        loading_recycler_view.adapter = loadingAdapter
        loading_recycler_view.visibility = View.GONE

        repo_recycler_view_container.setOnRefreshListener {
            viewModel.loadTrendingRepo(forceFetch = true)
        }

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

    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TrendingRepoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val baseUrl = (context.applicationContext as TrendingRepoApp).getBaseUrl()
                val githubService =
                    GithubTrendingServiceFactory.makeGithubService(BuildConfig.DEBUG, baseUrl)
                val remote = GithubTrendingRemoteDataStore(githubService)

                val database = TrendingRepoDatabase.getInstance(context)
                val sharedPref = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
                val localDataStore = GithubTrendingLocalDataStore(database, sharedPref)
                val repository =
                    GithubTrendingRepository(remote, localDataStore, Clock.systemDefaultZone())
                return TrendingRepoViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}