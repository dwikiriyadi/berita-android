package id.dwikiriyadi.berita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import id.dwikiriyadi.berita.R
import id.dwikiriyadi.berita.data.model.Data

class BeritaAdapter(val onItemClick: (item: Data) -> Unit) :
    PagedListAdapter<Data, BeritaViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            when (viewType) {
                0 -> R.layout.item_card_image
                1 -> R.layout.item_card_right_image
                else -> R.layout.item_card_text
            },
            parent,
            false
        )

        return BeritaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeritaViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item.data) {
                onItemClick(item)
            }
        }
    }

    override fun getItemViewType(position: Int) = position.rem(3)

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data) =
                oldItem.data.title == newItem.data.title

            override fun areContentsTheSame(oldItem: Data, newItem: Data) =
                oldItem.data == newItem.data
        }
    }
}