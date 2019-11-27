package id.dwikiriyadi.berita.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import id.dwikiriyadi.berita.BR
import id.dwikiriyadi.berita.data.model.Berita

class BeritaViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Berita, func: () -> Unit) {
        binding.setVariable(BR.data, data)
        binding.root.setOnClickListener {
            func()
        }
        binding.executePendingBindings()
    }
}