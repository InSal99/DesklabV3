package com.edts.desklabv3.features

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    context: Context,
    @DimenRes spaceResId: Int,
    private val orientation: Int
) : RecyclerView.ItemDecoration() {

    private val space = context.resources.getDimensionPixelSize(spaceResId)

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        // Do not add space BEFORE the first item
        if (position == 0) {
            return
        }

        // Apply space based on the orientation
        when (orientation) {
            HORIZONTAL -> outRect.left = space
            VERTICAL -> outRect.top = space
        }
    }
}