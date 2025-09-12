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
    private var tabTexts: Array<String> = arrayOf("Tab 1")
    private var selectedPosition: Int = 0
    private var onTabClickListener: OnTabClickListener? = null

    init {
        tabAdapter = TabAdapter(
            tabTexts = tabTexts,
            selectedPosition = selectedPosition
        ) { position, tabText ->
            onTabClickListener?.onTabClick(position, tabText)
        }

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = tabAdapter
        clipToPadding = false

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Tab,
            0, 0
        ).apply {
            try {
                val tabTextArray = getTextArray(R.styleable.Tab_tabTexts)
                if (tabTextArray != null && tabTextArray.isNotEmpty()) {
                    val textArray = Array(tabTextArray.size) { i -> tabTextArray[i].toString() }
                    setTabTexts(textArray)
                }

                val initialSelectedPosition = getInt(R.styleable.Tab_initialSelectedPosition, 0)
                setSelectedPosition(initialSelectedPosition, false)
            } finally {
                recycle()
            }
        }
    }

    fun setTabTexts(texts: Array<String>) {
        if (texts.isEmpty()) return

        tabTexts = texts

        if (selectedPosition >= texts.size) {
            selectedPosition = 0
        }

        tabAdapter.updateTabs(texts, selectedPosition)
    }

    fun setTabTexts(texts: List<String>) {
        setTabTexts(texts.toTypedArray())
    }

    fun getTabTexts(): Array<String> {
        return tabTexts.copyOf()
    }

    fun setBadgeText(position: Int, badgeText: String?) {
        if (position < 0 || position >= tabDataList.size) return

        tabDataList[position] = tabDataList[position].copy(badgeText = badgeText)
        tabAdapter.notifyItemChanged(position)
    }

    fun setBadgeVisibility(position: Int, showBadge: Boolean) {
        if (position < 0 || position >= tabDataList.size) return

        tabDataList[position] = tabDataList[position].copy(showBadge = showBadge)
        tabAdapter.notifyItemChanged(position)
    }

    fun setBadge(position: Int, badgeText: String?, showBadge: Boolean = true) {
        if (position < 0 || position >= tabDataList.size) return

        tabDataList[position] = tabDataList[position].copy(
            badgeText = badgeText,
            showBadge = showBadge
        )
        tabAdapter.notifyItemChanged(position)
    }

    fun getBadgeText(position: Int): String? {
        return if (position in tabDataList.indices) {
            tabDataList[position].badgeText
        } else {
            null
        }
    }

    fun isBadgeVisible(position: Int): Boolean {
        return if (position in tabDataList.indices) {
            tabDataList[position].showBadge
        } else {
            false
        }
    }

    fun setSelectedPosition(position: Int, notifyListener: Boolean = true) {
        if (position < 0 || position >= tabTexts.size || position == selectedPosition) {
            return
        }

        selectedPosition = position
        tabAdapter.updateSelectedPosition(position)

        if (notifyListener) {
            onTabClickListener?.onTabClick(position, tabTexts[position])
        }
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun getSelectedTabText(): String? {
        return if (selectedPosition in tabTexts.indices) {
            tabTexts[selectedPosition]
        } else {
            null
        }
    }

    fun setOnTabClickListener(listener: OnTabClickListener?) {
        this.onTabClickListener = listener
    }

    fun addTab(tabText: String) {
        val newTexts = tabTexts + tabText
        setTabTexts(newTexts)
    }

    fun insertTab(position: Int, tabText: String) {
        if (position < 0 || position > tabTexts.size) return

        val newTexts = tabTexts.toMutableList()
        newTexts.add(position, tabText)

        if (position <= selectedPosition) {
            selectedPosition++
        }

        setTabTexts(newTexts.toTypedArray())
    }

    fun removeTab(position: Int) {
        if (position < 0 || position >= tabTexts.size || tabTexts.size <= 1) return

        val newTexts = tabTexts.toMutableList()
        newTexts.removeAt(position)

        when {
            position < selectedPosition -> selectedPosition--
            position == selectedPosition -> {
                selectedPosition = if (selectedPosition > 0) selectedPosition - 1 else 0
            }
        }

        setTabTexts(newTexts.toTypedArray())
    }

    fun updateTabText(position: Int, newText: String) {
        if (position < 0 || position >= tabTexts.size) return

        tabTexts[position] = newText
        tabAdapter.notifyItemChanged(position)
    }

    fun getTabCount(): Int {
        return tabTexts.size
    }

    fun isPositionSelected(position: Int): Boolean {
        return position == selectedPosition
    }
}