package com.edts.components.tab

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TabAdapter(
    private var tabDataList: List<TabData>,
    private var selectedPosition: Int,
    private val onClick: (Int, String) -> Unit
) : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    inner class TabViewHolder(val tabItem: TabItem) : RecyclerView.ViewHolder(tabItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val item = TabItem(parent.context)
        return TabViewHolder(item)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val data = tabDataList[position]
        val isActive = position == selectedPosition

        holder.tabItem.apply {
            resetForBinding()

            tabText = data.text
            badgeText = data.badgeText
            showBadge = data.showBadge

            tabState = if (isActive) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE

            delegate = object : TabDelegate {
                override fun onTabClick(tabItem: TabItem, newState: TabItem.TabState, previousState: TabItem.TabState) {
                    val currentPosition = holder.adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION && currentPosition != selectedPosition) {
                        val currentData = tabDataList[currentPosition]
                        onClick(currentPosition, currentData.text)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = tabDataList.size

    fun updateTabs(newData: List<TabData>, selectedPosition: Int) {
        this.tabDataList = newData
        this.selectedPosition = selectedPosition
        notifyDataSetChanged()
    }

    fun updateSelectedPosition(position: Int) {
        val oldPos = selectedPosition
        selectedPosition = position

        recyclerView?.let { rv ->
            val oldViewHolder = rv.findViewHolderForAdapterPosition(oldPos) as? TabViewHolder
            oldViewHolder?.tabItem?.tabState = TabItem.TabState.INACTIVE

            val newViewHolder = rv.findViewHolderForAdapterPosition(position) as? TabViewHolder
            newViewHolder?.tabItem?.tabState = TabItem.TabState.ACTIVE
        }
    }
}