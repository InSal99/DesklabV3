package com.edts.components.tab

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R

class Tab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    interface OnTabClickListener {
        fun onTabClick(position: Int, tabText: String)
    }

    private var tabAdapter: TabAdapter
    private var tabDataList: MutableList<TabData> = mutableListOf()
    private var selectedPosition: Int = 0
    private var onTabClickListener: OnTabClickListener? = null

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        tabAdapter = TabAdapter(tabDataList, selectedPosition) { position, tabText ->
            setSelectedPosition(position)
            onTabClickListener?.onTabClick(position, tabText)
        }

        adapter = tabAdapter
        clipToPadding = false

        addItemDecoration(TabSpaceItemDecoration(context))

        context.theme.obtainStyledAttributes(attrs, R.styleable.Tab, 0, 0).apply {
            try {
                val initialSelectedPosition = getInt(R.styleable.Tab_initialSelectedPosition, 0)
                setSelectedPosition(initialSelectedPosition, false)
            } finally {
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        val xmlTabs = mutableListOf<TabData>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is TabItem) {
                xmlTabs.add(
                    TabData(
                        text = child.tabText ?: "Label",
                        badgeText = child.badgeText,
                        showBadge = child.showBadge,
                        state = child.tabState
                    )
                )
            }
        }

        removeAllViews()

        if (xmlTabs.isNotEmpty()) {
            tabDataList = xmlTabs.toMutableList()

            val firstActiveIndex = tabDataList.indexOfFirst { it.state == TabItem.TabState.ACTIVE }
            selectedPosition = if (firstActiveIndex != -1) {
                firstActiveIndex
            } else {
                0
            }

            tabDataList = tabDataList.mapIndexed { index, tab ->
                tab.copy(state = if (index == selectedPosition) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE)
            }.toMutableList()

            tabAdapter.updateTabs(tabDataList, selectedPosition)
        }
    }

    fun setOnTabClickListener(listener: OnTabClickListener?) {
        this.onTabClickListener = listener
    }

    fun setTabs(tabs: List<TabData>, selected: Int = 0) {
        tabDataList = tabs.toMutableList()
        selectedPosition = selected
        tabAdapter.updateTabs(tabDataList, selectedPosition)
    }

    fun setSelectedPosition(position: Int, notifyListener: Boolean = true) {
        if (position !in tabDataList.indices || position == selectedPosition) return

        val oldPos = selectedPosition
        selectedPosition = position

        tabDataList = tabDataList.mapIndexed { index, tab ->
            tab.copy(state = if (index == position) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE)
        }.toMutableList()

        // CHANGE: Use updateSelectedPosition instead of updateTabs to preserve animation
        tabAdapter.updateSelectedPosition(position)

        if (notifyListener) {
            onTabClickListener?.onTabClick(position, tabDataList[position].text)
        }
    }

    fun getSelectedPosition(): Int = selectedPosition
    fun getSelectedTabText(): String? = tabDataList.getOrNull(selectedPosition)?.text
    fun getTabCount(): Int = tabDataList.size
    fun isPositionSelected(position: Int): Boolean = position == selectedPosition

    private class TabSpaceItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
        private val space = context.resources.displayMetrics.density * 8

        override fun getItemOffsets(
            outRect: android.graphics.Rect,
            view: android.view.View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val itemCount = state.itemCount

            if (position < itemCount - 1) {
                outRect.right = space.toInt()
            }
        }
    }
}