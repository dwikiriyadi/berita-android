package id.dwikiriyadi.berita.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import id.dwikiriyadi.berita.data.model.Data
import id.dwikiriyadi.berita.data.model.NetworkState
import java.util.concurrent.Executor

class BeritaPageKeyedDataSource(
    private val query: HashMap<String, String>,
    private val retryExecutor: Executor
) : PageKeyedDataSource<Int, Data>() {

    private var retry: (() -> Any)? = null
    private val _state = MutableLiveData<NetworkState>()
    private val _error = MutableLiveData<String>()

    val state: LiveData<NetworkState>
        get() = _state

    val error: LiveData<String>
        get() = _error

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Data>
    ) {
        val currentPage = 1
        val nextPage = currentPage + 1

        query["page"] = currentPage.toString()

        _state.postValue(NetworkState.LOADING)

        getData(query, { body ->
            retry = null
            _state.postValue(NetworkState.LOADED)
            callback.onResult(body, null, nextPage)
        }, { error ->
            retry = { loadInitial(params, callback) }
            _state.postValue(NetworkState.ERROR)
            _error.postValue(error)
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {
        val currentPage = params.key
        val nextPage = currentPage + 1

        query["page"] = currentPage.toString()

        _state.postValue(NetworkState.LOADING)

        getData(query, { body ->
            retry = null
            _state.postValue(NetworkState.LOADED)
            callback.onResult(body, nextPage)
        }, { error ->
            retry = { loadAfter(params, callback) }
            _state.postValue(NetworkState.ERROR)
            _error.postValue(error)
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let { retry ->
            retryExecutor.execute { retry() }
        }
    }
}