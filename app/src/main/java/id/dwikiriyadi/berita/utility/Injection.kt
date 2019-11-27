package id.dwikiriyadi.berita.utility

import id.dwikiriyadi.berita.data.BeritaRepository
import id.dwikiriyadi.berita.ui.berita.BeritaViewModelFactory
import id.dwikiriyadi.berita.ui.main.MainViewModelFactory
import java.util.concurrent.Executors

object Injection {

    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    private fun provideBeritaRepository() = BeritaRepository(NETWORK_IO)

    fun provideMainViewModelFactory() = MainViewModelFactory(provideBeritaRepository())

    fun provideBeritaViewModelFactory() = BeritaViewModelFactory(provideBeritaRepository())
}