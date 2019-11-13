package id.dwikiriyadi.berita.utility

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val spaceHeight: Int): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = toDensityPixel(spaceHeight)
        }
        outRect.left = toDensityPixel(spaceHeight)
        outRect.right = toDensityPixel(spaceHeight)
        outRect.bottom = toDensityPixel(spaceHeight)
    }
}