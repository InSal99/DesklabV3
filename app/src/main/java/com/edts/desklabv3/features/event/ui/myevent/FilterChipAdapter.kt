package com.edts.desklabv3.features.event.ui.myevent

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.selection.Chip
import com.edts.desklabv3.features.event.model.FilterChip

class FilterChipAdapter(
    private val onChipClicked: (FilterChip) -> Unit
) : ListAdapter<FilterChip, FilterChipAdapter.ChipViewHolder>(FilterChipDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val chip = Chip(parent.context).apply {
            chipShowIcon = false
            chipShowBadge = false
            chipSize = Chip.ChipSize.MEDIUM
        }
        return ChipViewHolder(chip)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bind(getItem(position), onChipClicked)
    }

    class ChipViewHolder(private val chipView: Chip) : RecyclerView.ViewHolder(chipView) {
        private var lastClickTime = 0L

        fun bind(chipData: FilterChip, onChipClicked: (FilterChip) -> Unit) {
            chipView.chipText = chipData.text
            chipView.chipState = if (chipData.isSelected) Chip.ChipState.ACTIVE else Chip.ChipState.INACTIVE

//            if (chipData.isSelected) {
//                itemView.setOnClickListener(null)
//                itemView.isClickable = false
//            } else {
//                itemView.setOnClickListener { onChipClicked(chipData) }
//                itemView.isClickable = true
//            }

            if (chipData.isSelected) {
                itemView.setOnClickListener(null)
                itemView.isClickable = false
            } else {
                itemView.setOnClickListener {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime < 300) return@setOnClickListener
                    lastClickTime = currentTime

                    onChipClicked(chipData)
                }
                itemView.isClickable = true
            }
        }
    }

    private class FilterChipDiffCallback : DiffUtil.ItemCallback<FilterChip>() {
        override fun areItemsTheSame(oldItem: FilterChip, newItem: FilterChip): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: FilterChip, newItem: FilterChip): Boolean {
            return oldItem == newItem
        }
    }
}