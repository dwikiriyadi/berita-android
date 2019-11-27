package id.dwikiriyadi.berita.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import id.dwikiriyadi.berita.data.model.Berita
import id.dwikiriyadi.berita.data.model.BeritaResult
import id.dwikiriyadi.berita.data.model.NetworkState
import java.util.concurrent.Executor
import id.dwikiriyadi.berita.data.model.Result

class BeritaRepository(private val networkExecutor: Executor) {
    fun getData(query: HashMap<String, String>): Result {
        val dataSourceFactory = beritaDataSourceFactory(query)

        val data = LivePagedListBuilder(dataSourceFactory, 10)
            .setFetchExecutor(networkExecutor).build()

        val error: LiveData<String> =
            Transformations.switchMap(dataSourceFactory.source) { it.error }

        val networkState = Transformations.switchMap(dataSourceFactory.source) { it.state }

        return Result(
            data,
            error,
            networkState,
            { dataSourceFactory.source.value?.retryAllFailed() },
            { dataSourceFactory.source.value?.invalidate() })
    }

    fun getItem(uuid: String): BeritaResult {
        val query = HashMap<String, String>().apply { put("uuid", uuid) }
        val data = MutableLiveData<Berita>()
        val error = MutableLiveData<String>()
        val state = MutableLiveData<NetworkState>()

        state.postValue(NetworkState.LOADING)

        getData(query, { body ->
            state.postValue(NetworkState.LOADED)
            data.postValue(body[0].data)
        }, {
            state.postValue(NetworkState.ERROR)
            error.postValue(it)
        })

        return BeritaResult(data, error, state)
    }

    private fun beritaDataSourceFactory(searchQuery: HashMap<String, String>) =
        BeritaDataSourceFactory(searchQuery, networkExecutor)
}