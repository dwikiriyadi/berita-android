package id.dwikiriyadi.berita.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.ui.adapter.BeritaAdapter
import id.dwikiriyadi.berita.data.model.NetworkState
import id.dwikiriyadi.berita.databinding.FragmentMainBinding
import id.dwikiriyadi.berita.utility.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            Injection.provideMainViewModelFactory()
        )[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menerapkan layout pada fragment ini
        binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MainFragment
            viewmodel = this@MainFragment.viewModel
        }

        // Inisiasi variabel
        viewModel.search.observe(this, Observer {
            val query = HashMap<String, String>().apply {
                if (it.isNotEmpty())
                    put("title", it)
            }
            viewModel.searchData(query)
        })

        viewModel.networkError.observe(this, Observer {
            context!!.toast(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })

        // Implementasi Swipe Refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.search.postValue("")
            viewModel.getData()
        }

        initRecyclerView()

        viewModel.getData()

        return binding.root
    }

    private fun initRecyclerView() {
        val adapter = BeritaAdapter { item ->
            navController.navigate(
                R.id.action_mainFragment_to_beritaFragment,
                bundleOf("uuid" to item.data.uuid)
            )
        }

        // Implementasi RecyclerView
        binding.recyclerView.apply {
            // Set layoutManager, adapter, dan OnScrollListener
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MarginItemDecoration(16))
            this.adapter = adapter
        }

        viewModel.data.observe(this@MainFragment, Observer {
            adapter.submitList(it)
        })
    }

}
