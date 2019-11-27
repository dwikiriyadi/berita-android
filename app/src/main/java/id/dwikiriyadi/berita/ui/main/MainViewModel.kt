package id.dwikiriyadi.berita.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.dwikiriyadi.berita.data.BeritaRepository
import id.dwikiriyadi.berita.data.model.Result

class MainViewModel(repository: BeritaRepository) : ViewModel() {

    val search = MutableLiveData<String>()

    private val query = MutableLiveData<HashMap<String, String>>()
    private val result: LiveData<Result> = Transformations.map(query) {
        repository.getData(it)
    }

    val data = Transformations.switchMap(result) { it.data }
    val networkError = Transformations.switchMap(result) { it.error }
    val networkState = Transformations.switchMap(result) { it.networkState }

    fun searchData(query: HashMap<String, String>) {
        this.query.postValue(query)
    }

    fun getData() {
        this.query.postValue(HashMap())
    }
}