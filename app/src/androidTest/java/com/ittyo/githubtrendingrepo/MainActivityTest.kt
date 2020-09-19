package com.ittyo.githubtrendingrepo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ittyo.githubtrendingrepo.view.MainActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
                    .throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }

        activityRule.launchActivity(null)
        Thread.sleep(1000)
        onView(withId(R.id.loading_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.failed_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.repo_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun showRepoRecycleView_OnSuccessfulRequest() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
            }
        }

        activityRule.launchActivity(null)
        Thread.sleep(1000)
        onView(withId(R.id.loading_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.failed_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.repo_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun showFailedView_OnFailedRequest() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setHttp2ErrorCode(404)
                    .setBody("")
            }
        }

        activityRule.launchActivity(null)
        Thread.sleep(1000)
        onView(withId(R.id.loading_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.failed_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.repo_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
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

        onView(withId(R.id.loading_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.failed_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.repo_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.retry_button)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.loading_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.failed_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.repo_recycler_view)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

}