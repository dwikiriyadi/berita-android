package id.dwikiriyadi.berita.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout

import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.data.BeritaService
import id.dwikiriyadi.berita.data.model.Data
import id.dwikiriyadi.berita.utility.setImageFromUrl
import id.dwikiriyadi.berita.utility.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeritaFragment : Fragment() {

    private var uuid: String? = null
    private var listener: OnFragmentInteractionListener? = null

    // definisi widget
    private lateinit var toolbar: Toolbar
    private lateinit var content: ConstraintLayout
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var source: TextView
    private lateinit var date: TextView
    private lateinit var progress: ProgressBar
    private lateinit var notFound: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(ARG_UUID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_berita, container, false)

        // inisiasi widget
        toolbar = view.findViewById(R.id.toolbar)
        content = view.findViewById(R.id.content)
        image = view.findViewById(R.id.image)
        title = view.findViewById(R.id.title)
        body = view.findViewById(R.id.body)
        source = view.findViewById(R.id.source)
        date = view.findViewById(R.id.date)
        progress = view.findViewById(R.id.progress_circular)
        notFound = view.findViewById(R.id.not_found)

        toolbar.title = ""

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        // request data
        request()

        return view
    }

    private fun request() {
        // set query request
        val queries = HashMap<String, String>().apply {
            put("uuid", uuid!!)
        }

        BeritaService.create().getData(queries).enqueue(object : Callback<ArrayList<Data>> {
            override fun onFailure(call: Call<ArrayList<Data>>, t: Throwable) {
                // set visibility widget group
                content.visibility = View.GONE
                progress.visibility = View.GONE
                notFound.visibility = View.VISIBLE

                // menampilkan pesan error request data
                context!!.toast(t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<Data>>,
                response: Response<ArrayList<Data>>
            ) {
                if (response.isSuccessful) {
                    val item = response.body()!![0]
                    // set value widget
                    title.text = item.data.title
                    body.text = item.data.body
                    date.text = item.data.date
                    source.text = item.data.source
                    image.setImageFromUrl(item.data.image_cover)

                    // set visibility widget group
                    content.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                    notFound.visibility = View.GONE
                } else {
                    // set visibility widget group
                    content.visibility = View.GONE
                    progress.visibility = View.GONE
                    notFound.visibility = View.VISIBLE

                    // menampilkan pesan error request data
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private const val ARG_UUID = "uuid"

        @JvmStatic
        fun instance(uuid: String) =
            BeritaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UUID, uuid)
                }
            }
    }
}
