package id.dwikiriyadi.berita.common

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object PerfTestUtils {

    private fun getTestDir(): File {

        val rootDir = getAppContext().getExternalFilesDir(null)

        // Create a test data subdirectory.
        val testFileDir = File(rootDir, "test")

        if (!testFileDir.exists()) {
            if (!testFileDir.mkdirs()) {
                throw RuntimeException("Unable to create test file directory.")
            }
        }
        return testFileDir

    }

    private fun getAppContext(): Context {
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    fun getTestFile(filename: String): File {
        return File(getTestDir(), filename)
    }

    fun logDate(): String {
        val dateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}