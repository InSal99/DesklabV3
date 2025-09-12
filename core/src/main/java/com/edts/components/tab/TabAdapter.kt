package com.edts.components.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.databinding.ItemTabBinding

class TabAdapter(
    private var tabTexts: Array<String>,
    private var selectedPosition: Int,
    private val onTabClick: (position: Int, tabText: String) -> Unit
) : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {

    init {
        if (selectedPosition < 0 || selectedPosition >= tabTexts.size) {
            selectedPosition = 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding = ItemTabBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(tabTexts[position], position == selectedPosition, position)
    }

    override fun getItemCount(): Int = tabTexts.size

    fun updateSelectedPosition(newPosition: Int) {
        if (newPosition < 0 || newPosition >= tabTexts.size || newPosition == selectedPosition) {
            return
        }

        val previousPosition = selectedPosition
        selectedPosition = newPosition

        notifyItemChanged(previousPosition)
        notifyItemChanged(newPosition)
    }

    fun updateTabs(newTabTexts: Array<String>, newSelectedPosition: Int) {
        tabTexts = newTabTexts
        selectedPosition = if (newSelectedPosition >= 0 && newSelectedPosition < newTabTexts.size) {
            newSelectedPosition
        } else {
            0
        }
        notifyDataSetChanged()
    }

    fun getSelectedTabText(): String? {
        return if (selectedPosition in tabTexts.indices) {
            tabTexts[selectedPosition]
        } else {
            null
        }
    }

    inner class TabViewHolder(private val binding: ItemTabBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tabText: String, isSelected: Boolean, position: Int) {
            binding.cvTab.apply {
                this.tabText = tabText
                tabState = if (isSelected) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE

                setOnClickListener {
                    if (!isSelected && position != selectedPosition) {
                        updateSelectedPosition(position)
                        onTabClick(position, tabText)
                    }
                }
                isClickable = !isSelected
            }
        }
    }
}