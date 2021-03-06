package com.ittyo.githubtrendingrepo.view

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ittyo.githubtrendingrepo.repository.Repo
import com.ittyo.githubtrendingrepo.repository.Result
import com.ittyo.githubtrendingrepo.repository.TrendingRepository
import kotlinx.coroutines.launch

class TrendingRepoViewModel @ViewModelInject constructor(private val repository: TrendingRepository): ViewModel() {

    val stateLiveData = MutableLiveData<State>()

    fun loadTrendingRepo(forceFetch: Boolean = false) {
        viewModelScope.launch {
            stateLiveData.value = State.Loading
            stateLiveData.value = when (val result = repository.getTrendingRepo(forceFetch)) {
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