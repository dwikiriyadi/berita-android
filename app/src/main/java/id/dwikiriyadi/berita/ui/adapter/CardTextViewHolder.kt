package id.dwikiriyadi.berita.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.data.model.Berita

class CardTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val root = view.findViewById<CardView>(R.id.root)
    private val title = view.findViewById<TextView>(R.id.title)
    private val time = view.findViewById<TextView>(R.id.time)
    private val source = view.findViewById<TextView>(R.id.source)

    fun bind(data: Berita, func: () -> Unit) {
        title.text = data.title
        source.text = data.source
        time.text = data.date
        root.setOnClickListener {
            func()
        }
    }
}