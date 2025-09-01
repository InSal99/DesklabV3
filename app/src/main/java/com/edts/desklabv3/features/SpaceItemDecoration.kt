package com.example.desklabv3.features

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/**
 * A SpaceItemDecoration for adding space to items in a list.
 *
 * @param context The context to access resources.
 * @param spaceResId The dimension resource ID for the desired spacing.
 * @param orientation The orientation of the list (VERTICAL or HORIZONTAL).
 */
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
        val itemCount = state.itemCount

        if (position == itemCount - 1) {
            return
        }

        // Apply space based on the orientation
        when (orientation) {
            HORIZONTAL -> outRect.right = space
            VERTICAL -> outRect.bottom = space
        }
    }
}