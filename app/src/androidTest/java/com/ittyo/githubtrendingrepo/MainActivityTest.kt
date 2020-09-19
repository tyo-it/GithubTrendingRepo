package com.ittyo.githubtrendingrepo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ittyo.githubtrendingrepo.util.FileReader
import com.ittyo.githubtrendingrepo.view.MainActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val mockWebServer = MockWebServer()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun setup() {
        mockWebServer.start(8080)
        val resource = OkHttp3IdlingResource.create("OkHttp", OkHttpClient())
        IdlingRegistry.getInstance().register(resource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun showLoadingView_OnWaitingRequestResponse() {
        val successResponse = MockResponse().setHttp2ErrorCode(404).setBody("")
            .throttleBody(1024, 1, TimeUnit.SECONDS)
        mockWebServer.enqueue(successResponse)

        activityRule.launchActivity(null)

        Thread.sleep(1000)

        checkLoadingIsShown()
    }

    @Test
    fun showRepoRecycleView_OnSuccessfulRequest() {
        val successResponse = MockResponse().setResponseCode(200).setBody(
            FileReader.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(successResponse)

        activityRule.launchActivity(null)
        Thread.sleep(1000)

        checkSuccessResultIsShown()
    }

    @Test
    fun showFailedView_OnFailedRequest() {
        val failedResponse = MockResponse().setHttp2ErrorCode(404).setBody("")
        mockWebServer.enqueue(failedResponse)

        activityRule.launchActivity(null)

        Thread.sleep(1000)

        checkFailedResultIsShown()
    }

    @Test
    fun whenFailed_clickRetry_willStartNewRequest() {
        val failedResponse = MockResponse().setHttp2ErrorCode(404).setBody("")
        val successResponse = MockResponse().setResponseCode(200).setBody(
            FileReader.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(failedResponse)
        mockWebServer.enqueue(successResponse)

        activityRule.launchActivity(null)

        Thread.sleep(1000)

        checkFailedResultIsShown()

        clickRetryButton()

        Thread.sleep(1000)

        checkSuccessResultIsShown()
    }

    @Test
    fun whenSwipeOnRepoList_willStartNewRequest() {
        val failedResponse = MockResponse().setHttp2ErrorCode(404).setBody("")
        val successResponse = MockResponse().setResponseCode(200).setBody(
            FileReader.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(successResponse)
        mockWebServer.enqueue(failedResponse)

        activityRule.launchActivity(null)

        Thread.sleep(1000)

        checkSuccessResultIsShown()

        swipeOnRepoList()

        Thread.sleep(1000)

        checkFailedResultIsShown()
    }

    companion object ScreenHelper {

        fun clickRetryButton() {
            onView(withId(R.id.retry_button)).perform(click())
        }

        fun swipeOnRepoList() {
            onView(withId(R.id.repo_recycler_view_container)).perform(swipeDown())
        }

        fun checkSuccessResultIsShown() {
            onView(withId(R.id.loading_recycler_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
            onView(withId(R.id.failed_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
            onView(withId(R.id.repo_recycler_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        }

        fun checkFailedResultIsShown() {
            onView(withId(R.id.loading_recycler_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
            onView(withId(R.id.failed_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
            onView(withId(R.id.repo_recycler_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }

        fun checkLoadingIsShown() {
            onView(withId(R.id.loading_recycler_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
            onView(withId(R.id.failed_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
            onView(withId(R.id.repo_recycler_view)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        }

    }
}