package id.dwikiriyadi.berita.utility

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun toDensityPixel(value: Int) =
    (value * Resources.getSystem().displayMetrics.density).roundToInt()

fun FragmentManager.setFragment(container: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    if (addToBackStack) {
        this.beginTransaction().replace(container, fragment).addToBackStack(null).commit()
    } else {
        this.beginTransaction().replace(container, fragment).commit()
    }
}

fun ImageView.setImageFromUrl(imageUrl: String) {
    Glide.with(this.context)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}

fun logDate(): String {
    val dateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.getDefault())
    return dateFormat.format(Date())
}