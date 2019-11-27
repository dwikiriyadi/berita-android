package id.dwikiriyadi.berita.ui.berita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.dwikiriyadi.berita.data.BeritaRepository

class BeritaViewModelFactory(private val repository: BeritaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeritaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BeritaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}