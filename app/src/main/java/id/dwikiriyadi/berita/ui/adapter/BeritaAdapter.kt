package id.dwikiriyadi.berita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.data.model.Data

class BeritaAdapter(val onItemClick: (item: Data) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            when (viewType) {
                0 -> R.layout.item_card_image
                1 -> R.layout.item_card_right_image
                else -> R.layout.item_card_text
            },
            parent,
            false
        )

        return when (viewType) {
            0 -> CardImageViewHolder(view)
            1 -> CardRightImageViewHolder(view)
            else -> CardTextViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = items[position]

        when (holder.itemViewType) {
            0 -> (holder as CardImageViewHolder).bind(item.data) {
                onItemClick(item)
            }
            1 -> (holder as CardRightImageViewHolder).bind(item.data) {
                onItemClick(item)
            }
            else -> (holder as CardTextViewHolder).bind(item.data) {
                onItemClick(item)
            }
        }
    }

    override fun getItemViewType(position: Int) = position.rem(3)

    override fun getItemCount() = items.size

    fun setList(data: ArrayList<Data>) {
        this.items.clear()
        this.items = data
        notifyDataSetChanged()
    }

    fun addData(data: ArrayList<Data>) {
        this.items.addAll(data)
        notifyDataSetChanged()
    }
}