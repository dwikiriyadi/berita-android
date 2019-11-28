package id.dwikiriyadi.berita

import android.os.Debug
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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import id.dwikiriyadi.berita.data.OkHttpProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.*
import org.junit.runner.RunWith
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun start() {

    }

    @After
    fun finish() {
        val dateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.getDefault())
        val logDate = dateFormat.format(Date())

        val megaByte = 1024L * 1024L
        val nativeHeapSize = Debug.getNativeHeapSize()
        val nativeHeapFreeSize = Debug.getNativeHeapFreeSize()
        val nativeHeapSizeInMB = Debug.getNativeHeapSize() / megaByte
        val nativeHeapFreeSizeInMB = Debug.getNativeHeapFreeSize() / megaByte
        val usedMemoryInByte = nativeHeapSize - nativeHeapFreeSize
        val usedMemoryInMB = usedMemoryInByte / megaByte
        val usedMemoryInPercentage = usedMemoryInByte * 100 / nativeHeapSize

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(context.getExternalFilesDir(null), "memory-usage-$logDate.txt")
        FileWriter(file).apply {
            append("Native Heap Size : $nativeHeapSize byte")
            append(System.lineSeparator())
            append("Native Heap Size (MB) : $nativeHeapSizeInMB megabyte")
            append(System.lineSeparator())
            append("Native Heap Free Size : $nativeHeapFreeSize byte")
            append(System.lineSeparator())
            append("Native Heap Free Size (MB) : $nativeHeapFreeSizeInMB megabyte")
            append(System.lineSeparator())
            append("Used Memory In Bytes : $usedMemoryInByte byte")
            append(System.lineSeparator())
            append("Used Memory In MBs : $usedMemoryInMB megabyte")
            append(System.lineSeparator())
            append("Used Memory In Percentages : $usedMemoryInPercentage %")
            flush()
            close()
        }
    }

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
