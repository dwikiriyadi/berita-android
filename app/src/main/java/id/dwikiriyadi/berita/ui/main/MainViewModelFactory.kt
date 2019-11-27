package id.dwikiriyadi.berita.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.dwikiriyadi.berita.data.BeritaRepository

class MainViewModelFactory(private val repository: BeritaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}