package id.dwikiriyadi.berita

import android.os.Debug
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import id.dwikiriyadi.berita.common.PerfTestUtils
import id.dwikiriyadi.berita.data.OkHttpProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.util.logging.Level
import java.util.logging.Logger

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val logger = Logger.getLogger(MainActivityTest::class.java.name)

    private fun netStatsDumpStart() {
        try {
            val builder = ProcessBuilder()
            builder.command("dumpsys", "netstats", "--reset")
            val process = builder.start()
            process.waitFor()
        } catch (exception: Exception) {
            logger.log(Level.SEVERE, "Unable to reset dumpsys", exception)
        }
    }

    private fun netStatsDumpFinish() {
        var fileWriter: FileWriter? = null
        var bufferedReader: BufferedReader? = null
        try {
            val processBuilder = ProcessBuilder()

            processBuilder.command("dumpsys", "netstats", "full", "detail")
            processBuilder.redirectErrorStream()
            val process = processBuilder.start()

            val mLogFileAbsoluteLocation = PerfTestUtils.getTestFile(
                "netstats${PerfTestUtils.logDate()}.dumpsys.log"
            )

            fileWriter = FileWriter(mLogFileAbsoluteLocation)
            bufferedReader = BufferedReader(
                InputStreamReader(process.inputStream)
            )

            while (bufferedReader.readLine() != null) {
                fileWriter.append(bufferedReader.readLine())
                fileWriter.append(System.lineSeparator())
            }
            process.waitFor()
            if (process.exitValue() != 0) {
                throw Exception("Error while taking dumpsys, exitCode=" + process.exitValue())
            }
        } catch (exception: Exception) {
            logger.log(Level.SEVERE, "Unable to take a dumpsys", exception)
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        Debug.startMethodTracing("method-tracing-${PerfTestUtils.logDate()}")
        netStatsDumpStart()
    }

    @After
    fun finish() {
        Debug.stopMethodTracing()
        netStatsDumpFinish()
    }

    @Test
    fun mainActivityTest() {
        val resource : IdlingResource = OkHttp3IdlingResource.create("okhttp", OkHttpProvider.okHttpInstance)
        IdlingRegistry.getInstance().register(resource)

        // SplashScreen Test
        Thread.sleep(3000)

        // RecyclerView Test
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).check(checkRecyclerViewItemCount(10))

        // SwipeRefresh Test
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).perform(swipeDown())
        checkSwipeRefresh()
        onView(allOf<View>(withId(R.id.recycler_view), isDisplayed())).check(checkRecyclerViewItemCount(10))


        // Find News Test
        onView(allOf<View>(withId(R.id.search_edit), isDisplayed())).perform(replaceText("janji"), closeSoftKeyboard())

        // Navigate to BeritaFragment
        onView(
            allOf(
                withId(R.id.root),
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(R.id.swipe_refresh),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        // Back to MainFragment
        onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.appbar),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())
        IdlingRegistry.getInstance().unregister(resource)
    }

/*    @Test
    fun mainActivityTest() {

        val appCompatEditText = onView(
            allOf(
                withId(R.id.search_edit),
                childAtPosition(
                    allOf(
                        withId(R.id.search_bar),
                        childAtPosition(
                            withId(R.id.appbar),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("janji"), closeSoftKeyboard())

        pressBack()

        val cardView = onView(
            allOf(
                withId(R.id.root),
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(R.id.swipe_refresh),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())
    }*/

    private fun checkRecyclerViewItemCount(count: Int) = object: ViewAssertion {
        override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            assertThat(recyclerView.adapter!!.itemCount, `is`(count))
        }
    }

    private fun checkSwipeRefresh() {
        val swRefresh =
            mActivityTestRule.activity.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout
        Assert.assertNotNull(swRefresh)
        Assert.assertTrue(!swRefresh.isRefreshing)
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
