package id.dwikiriyadi.berita

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import id.dwikiriyadi.berita.ui.BeritaFragment
import id.dwikiriyadi.berita.ui.MainFragment
import id.dwikiriyadi.berita.ui.SplashFragment
import id.dwikiriyadi.berita.utility.setFragment

class MainActivity : AppCompatActivity(),
    SplashFragment.OnFragmentInteractionListener,
    MainFragment.OnFragmentInteractionListener,
    BeritaFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)

        if (fragmentContainer != null) {
            if (savedInstanceState != null) {
                return
            }

            supportFragmentManager.setFragment(R.id.fragment_container, SplashFragment())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onFragmentInteraction(uri: Uri) {}
}
