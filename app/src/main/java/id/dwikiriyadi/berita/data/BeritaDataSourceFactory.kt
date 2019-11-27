package id.dwikiriyadi.berita.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.dwikiriyadi.berita.data.model.Data
import java.util.concurrent.Executor

class BeritaDataSourceFactory(
    private val searchQuery: HashMap<String, String>,
    private val retryExecutor: Executor
): DataSource.Factory<Int, Data>() {

    val source = MutableLiveData<BeritaPageKeyedDataSource>()

    override fun create(): DataSource<Int, Data> {
        val source = BeritaPageKeyedDataSource(searchQuery, retryExecutor)
        this.source.postValue(source)
        return source
    }
}