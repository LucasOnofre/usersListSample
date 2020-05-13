package com.onoffrice.userslistsample.presentation.users

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.onoffrice.userslistsample.R
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class UsersActivityTest {

    private var server = MockWebServer()

    @get:Rule
    val activityRule = ActivityTestRule(UsersActivity::class.java, false, false)

    @Test
    fun test_isActivityInView() {
        launchActivity<UsersActivity>().apply {
            onView(withId(R.id.mainContainer)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun test_headerDisplayTitleInView() {
        launchActivity<UsersActivity>().apply {
            onView(withId(R.id.title)).check(matches(isDisplayed()))
            onView(withText(R.string.title)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayListItem() {
        server.start(serverPort)

        launchActivity<UsersActivity>().apply {
            server.enqueue(successResponse)
            onView(withId(R.id.usersRv)).check(matches(isDisplayed()))
        }

        server.close()
    }


    /** In case, it will work only in the first time, because after the first success
     * the app caches the data, and the, even if an error occurs, the list will be displayed with the
     * cached data**/
    @Test
    fun shouldNotDisplayListItem() {
        server.start(serverPort)

        launchActivity<UsersActivity>().apply {
            server.enqueue(errorResponse)
            onView(withId(R.id.usersRv)).check(matches((withEffectiveVisibility(Visibility.GONE))))
        }

        server.close()
    }

    companion object {
        private const val serverPort = 8080

        private val successResponse by lazy {
            val body =
                "[{\"id\":123,\"name\":\"Name Teste\",\"img\":\"https://randomuser.me/api/portraits/men/9.jpg\",\"username\":\"@teste\"}]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse by lazy { MockResponse().setResponseCode(404) }
    }
}
