package id.dwikiriyadi.berita.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

import id.dwikiriyadi.berita.data.model.Data
import retrofit2.Callback
import retrofit2.Response

fun getData(
    query: Map<String, String>,
    onSuccess: (body: List<Data>) -> Unit,
    onFail: (error: String) -> Unit
) {
    BeritaService.create().getData(query).enqueue(object : Callback<ArrayList<Data>> {
        override fun onFailure(call: Call<ArrayList<Data>>, t: Throwable) {
            onFail(t.message ?: "Unknown Error")
        }

        override fun onResponse(
            call: Call<ArrayList<Data>>,
            response: Response<ArrayList<Data>>
        ) {
            if (response.isSuccessful) {
                onSuccess(response.body() ?: emptyList())
            } else {
                onFail(response.errorBody()?.string() ?: "Unknown Error")
            }
        }
    })
}

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