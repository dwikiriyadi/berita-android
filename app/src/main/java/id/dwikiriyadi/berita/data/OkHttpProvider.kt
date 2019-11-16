package id.dwikiriyadi.berita.data

import okhttp3.OkHttpClient

object OkHttpProvider {
    private var instance: OkHttpClient? = null

    val okHttpInstance: OkHttpClient
        get() {
            if (instance == null) {
                instance = OkHttpClient()
            }
            return instance!!
        }
}