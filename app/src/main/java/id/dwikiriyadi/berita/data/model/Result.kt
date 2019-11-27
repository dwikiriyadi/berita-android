package id.dwikiriyadi.berita.data.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Result (
    val data: LiveData<PagedList<Data>>,
    val error: LiveData<String>,
    val networkState: LiveData<NetworkState>,
    val retry: () -> Unit,
    val refresh: () -> Unit
)

data class BeritaResult(
    val data: LiveData<Berita>,
    val error: LiveData<String>,
    val networkState: LiveData<NetworkState>
)