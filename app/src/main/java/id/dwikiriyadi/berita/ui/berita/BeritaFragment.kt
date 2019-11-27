package id.dwikiriyadi.berita.ui.berita

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import id.dwikiriyadi.berita.databinding.FragmentBeritaBinding
import id.dwikiriyadi.berita.utility.Injection
import id.dwikiriyadi.berita.utility.toast

class BeritaFragment : Fragment() {

    private lateinit var viewModel: BeritaViewModel
    private lateinit var binding: FragmentBeritaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            Injection.provideBeritaViewModelFactory()
        )[BeritaViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBeritaBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@BeritaFragment
            viewmodel = this@BeritaFragment.viewModel
        }

        binding.toolbar.title = ""

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.getBerita(arguments!!.getString("uuid")!!)

        viewModel.data.observe(this, Observer {
            binding.content.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE
            binding.notFound.visibility = View.GONE
        })

        viewModel.networkError.observe(this, Observer {
            binding.content.visibility = View.GONE
            binding.progressCircular.visibility = View.GONE
            binding.notFound.visibility = View.VISIBLE
            context!!.toast(it)
        })

        return binding.root
    }
}
