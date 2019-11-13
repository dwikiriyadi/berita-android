package id.dwikiriyadi.berita.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.data.model.Berita
import id.dwikiriyadi.berita.utility.setImageFromUrl

class CardImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var root: CardView = view.findViewById(R.id.root)
    private val image = view.findViewById<ImageView>(R.id.image)
    private val title = view.findViewById<TextView>(R.id.title)
    private val time = view.findViewById<TextView>(R.id.time)
    private val source = view.findViewById<TextView>(R.id.source)

    fun bind(data: Berita, func: () -> Unit) {
        title.text = data.title
        source.text = data.source
        time.text = data.date
        image.setImageFromUrl(data.image_cover)
        root.setOnClickListener {
            func()
        }
    }
}