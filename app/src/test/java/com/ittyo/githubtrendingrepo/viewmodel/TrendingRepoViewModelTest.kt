package com.ittyo.githubtrendingrepo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ittyo.githubtrendingrepo.createRepo
import com.ittyo.githubtrendingrepo.repository.Result
import com.ittyo.githubtrendingrepo.repository.TrendingRepository
import com.ittyo.githubtrendingrepo.view.TrendingRepoViewModel
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class TrendingRepoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: TrendingRepository

    @Mock
    lateinit var observer: Observer<TrendingRepoViewModel.State>

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TrendingRepoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        viewModel = TrendingRepoViewModel(repository)
    }

    @After
    fun exit() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given getTrendingRepo return error, should show loading then failed state`() = runBlockingTest {
        whenever(repository.getTrendingRepo(true)).thenReturn(Result.Failed(IOException()))

        viewModel.stateLiveData.observeForever(observer)
        viewModel.loadTrendingRepo(true)

        val order = inOrder(observer)
        order.verify(observer).onChanged(TrendingRepoViewModel.State.Loading)
        order.verify(observer).onChanged(TrendingRepoViewModel.State.Failed)
    }

    @Test
    fun `given getTrendingRepo return success, should show loading then success state`() = runBlockingTest {
        val repos = listOf(createRepo("ittyo"))
        whenever(repository.getTrendingRepo(true)).thenReturn(Result.Success(repos))

        viewModel.stateLiveData.observeForever(observer)
        viewModel.loadTrendingRepo(true)

        val order = inOrder(observer)
        order.verify(observer).onChanged(TrendingRepoViewModel.State.Loading)
        order.verify(observer).onChanged(TrendingRepoViewModel.State.Success(repos))
    }

    @Test
    fun `should pass forceFetch parameter into loadTrendingRepo`() = runBlockingTest {
        whenever(repository.getTrendingRepo(false)).thenReturn(Result.Failed(IOException()))

        viewModel.loadTrendingRepo(false)

        verify(repository).getTrendingRepo(false)
    }
}