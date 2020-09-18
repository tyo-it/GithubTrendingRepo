package com.ittyo.githubtrendingrepo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ittyo.githubtrendingrepo.repository.GithubTrendingRepository
import com.ittyo.githubtrendingrepo.repository.Repo
import com.ittyo.githubtrendingrepo.repository.Result
import kotlinx.coroutines.launch

class TrendingRepoViewModel(private val repository: GithubTrendingRepository): ViewModel() {

    val stateLiveData = MutableLiveData<State>()

    fun loadTrendingRepo() {
        viewModelScope.launch {
            stateLiveData.value = State.Loading
            val result = repository.getTrendingRepo()
            stateLiveData.value = when(result) {
                is Result.Success -> State.Success(result.data)
                is Result.Failed -> State.Failed
            }
        }
    }

    sealed class State {
        object Loading: State()
        data class Success(val data: List<Repo>): State()
        object Failed: State()
    }
}