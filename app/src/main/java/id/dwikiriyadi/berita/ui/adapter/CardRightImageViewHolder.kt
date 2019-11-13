package id.dwikiriyadi.berita.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.data.model.Berita
import id.dwikiriyadi.berita.utility.setImageFromUrl

class CardRightImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val root: CardView = view.findViewById(R.id.root)
    val image: ImageView = view.findViewById(R.id.image)
    val title: TextView = view.findViewById(R.id.title)
    val time: TextView = view.findViewById(R.id.time)
    val source: TextView = view.findViewById(R.id.source)

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