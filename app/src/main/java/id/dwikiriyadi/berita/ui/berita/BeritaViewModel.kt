package id.dwikiriyadi.berita.ui.berita

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.dwikiriyadi.berita.data.BeritaRepository
import id.dwikiriyadi.berita.data.model.BeritaResult

class BeritaViewModel(repository: BeritaRepository) : ViewModel() {
    private val uuid = MutableLiveData<String>()

    private val result: LiveData<BeritaResult> = Transformations.map(uuid) {
        repository.getItem(it)
    }

    val data = Transformations.switchMap(result) { it.data }
    val networkError = Transformations.switchMap(result) { it.error }

    fun getBerita(uuid: String) {
        this.uuid.postValue(uuid)
    }
}