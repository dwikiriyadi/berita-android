package id.dwikiriyadi.berita.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.ui.adapter.BeritaAdapter
import id.dwikiriyadi.berita.utility.setFragment
import id.dwikiriyadi.berita.utility.InfiniteScrollListener
import id.dwikiriyadi.berita.utility.MarginItemDecoration
import id.dwikiriyadi.berita.data.BeritaService
import id.dwikiriyadi.berita.data.model.Data
import id.dwikiriyadi.berita.utility.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    // definisi variabel
    private lateinit var searchEdit: EditText
    private lateinit var recyclerViewAdapter: BeritaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menerapkan layout pada fragment ini
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // Inisiasi variabel
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        recyclerView = view.findViewById(R.id.recycler_view)
        searchEdit = view.findViewById(R.id.search_edit)
        recyclerViewAdapter = BeritaAdapter { item ->
            fragmentManager!!.setFragment(
                R.id.fragment_container,
                BeritaFragment.instance(item.data.uuid), true
            )
        }

        var page = 1

        // Implementasi RecyclerView
        recyclerView.apply {
            /*  Membuat variabel LinearLayoutManager
             *  agar dapat digunakan event dan setup layoutManager */
            val linearLayoutManager = LinearLayoutManager(context)

            // Set layoutManager, adapter, dan OnScrollListener
            layoutManager = linearLayoutManager
            addItemDecoration(MarginItemDecoration(16))
            adapter = recyclerViewAdapter
            addOnScrollListener(InfiniteScrollListener({
                request(++page)
            }, linearLayoutManager))
        }

        // Implementasi SearchBar
        searchEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                request()
            }
        })

        // Implementasi Swipe Refresh
        swipeRefresh.setOnRefreshListener {
            searchEdit.setText("")
            request()
        }

        request()

        return view
    }

    // fungsi request data ke server
    private fun request(page: Int = 1) {
        swipeRefresh.isRefreshing = true
        val queries = HashMap<String, String>().apply {
            put("page", page.toString())
            if (searchEdit.text.isNotEmpty()) {
                put("title", searchEdit.text.toString())
            }
        }

        BeritaService.create().getData(queries).enqueue(object : Callback<ArrayList<Data>> {
            override fun onFailure(call: Call<ArrayList<Data>>, t: Throwable) {
                context!!.toast(t.message!!)
            }

            override fun onResponse(
                call: Call<ArrayList<Data>>,
                response: Response<ArrayList<Data>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    recyclerViewAdapter.apply {
                        if (page ==  1) {
                            setList(body)
                        } else {
                            addData(body)
                        }
                    }
                    swipeRefresh.isRefreshing = false
                } else {
                    context!!.toast(response.errorBody().toString())
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
