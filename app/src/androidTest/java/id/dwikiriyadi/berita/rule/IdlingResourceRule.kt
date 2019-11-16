package id.dwikiriyadi.berita.rule

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import com.jakewharton.espresso.OkHttp3IdlingResource
import id.dwikiriyadi.berita.data.OkHttpProvider
import okhttp3.OkHttpClient
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class IdlingResourceRule : TestRule {

    private val resource : IdlingResource = OkHttp3IdlingResource.create("okhttp", OkHttpProvider.okHttpInstance)

    override fun apply(base: Statement?, description: Description?) = object: Statement() {
        override fun evaluate() {
            IdlingRegistry.getInstance().register(resource)
            base!!.evaluate()
            IdlingRegistry.getInstance().unregister(resource)
        }
    }
}