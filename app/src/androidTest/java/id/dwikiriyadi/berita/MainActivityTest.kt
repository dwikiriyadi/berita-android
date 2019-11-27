package id.dwikiriyadi.berita

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import id.dwikiriyadi.berita.data.OkHttpProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val resource : IdlingResource = OkHttp3IdlingResource.create("okhttp", OkHttpProvider.okHttpInstance)
        IdlingRegistry.getInstance().register(resource)

        // SplashScreenTest
        Thread.sleep(3000)

        // RecyclerView Test
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).check(checkRecyclerViewItemCount(10))

        // SwipeRefresh Test
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).perform(swipeDown())
        checkSwipeRefresh()
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).check(checkRecyclerViewItemCount(10))

        // onScroll Test
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).perform(swipeUp())
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).check(checkRecyclerViewItemCount(20))

        // Find News Test
        onView(allOf<View>(withId(R.id.search_edit), isDisplayed())).perform(replaceText("janji"), closeSoftKeyboard())

        // Navigate to BeritaFragment
        onView(allOf<View>(withId(R.id.recycler_view))).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Back to MainFragment
        onView(allOf(withContentDescription("Navigate up"), isDisplayed())).perform(click())

        IdlingRegistry.getInstance().unregister(resource)
    }

    private fun checkRecyclerViewItemCount(count: Int) =
        ViewAssertion { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            assertThat(recyclerView.adapter!!.itemCount, `is`(count))
        }

    private fun checkSwipeRefresh() {
        val swRefresh =
            mActivityTestRule.activity.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout
        Assert.assertNotNull(swRefresh)
        Assert.assertTrue(!swRefresh.isRefreshing)
    }
}
