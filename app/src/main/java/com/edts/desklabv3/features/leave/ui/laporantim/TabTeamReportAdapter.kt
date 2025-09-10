package com.edts.desklabv3.features.leave.ui.laporantim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.tab.Tab
import com.edts.desklabv3.databinding.ItemTabBinding

class TabTeamReportAdapter(
    private var tabTexts: Array<String> = arrayOf("Aktivitas", "Cuti"),
    private var selectedPosition: Int = 0,
    private val onTabClick: (position: Int, tabText: String) -> Unit
) : RecyclerView.Adapter<TabTeamReportAdapter.TabViewHolder>() {

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

    inner class TabViewHolder(private val binding: ItemTabBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tabText: String, isSelected: Boolean, position: Int) {
            binding.cvTab.apply {
                this.tabText = tabText
                tabState = if (isSelected) Tab.TabState.ACTIVE else Tab.TabState.INACTIVE

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