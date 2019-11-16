package id.dwikiriyadi.berita.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

import id.dwikiriyadi.berita.data.model.Data

interface BeritaService {

    @GET("/")
    fun getData(@QueryMap queries: Map<String, String>? = null): Call<ArrayList<Data>>

    companion object {

        private const val URL = "http://berita.proses.xyz"

        fun create(): BeritaService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL)
                .client(OkHttpProvider.okHttpInstance)
                .build().create(BeritaService::class.java)
        }
    }
}