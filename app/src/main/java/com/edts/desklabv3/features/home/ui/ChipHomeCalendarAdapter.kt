package com.edts.desklabv3.features.home.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.chip.Chip
import com.edts.components.R
import com.edts.desklabv3.databinding.ItemChipBinding

class ChipHomeCalendarAdapter(
    private var chipTexts: Array<String>,
    private var selectedPosition: Int = 0,
    private val onChipClick: (position: Int, chipText: String) -> Unit
) : RecyclerView.Adapter<ChipHomeCalendarAdapter.ChipViewHolder>() {

    init {
        if (selectedPosition < 0 || selectedPosition >= chipTexts.size) {
            selectedPosition = 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bind(chipTexts[position], position == selectedPosition, position)
    }

    override fun getItemCount(): Int = chipTexts.size

    fun updateSelectedPosition(newPosition: Int) {
        if (newPosition < 0 || newPosition >= chipTexts.size || newPosition == selectedPosition) {
            return
        }

        val previousPosition = selectedPosition
        selectedPosition = newPosition

        notifyItemChanged(previousPosition)
        notifyItemChanged(newPosition)
    }

    fun updateChipTexts(newChipTexts: Array<String>) {
        chipTexts = newChipTexts
        selectedPosition = if (newChipTexts.isNotEmpty()) 0 else -1
        notifyDataSetChanged()
    }

    private fun getChipBackgroundColor(chipText: String, context: android.content.Context): Int {
        return when (chipText) {
            "Semua" -> resolveColorAttribute(R.attr.colorBackgroundPrimaryInverse, context)
            "Event" -> resolveColorAttribute(R.attr.colorBackgroundUtilityEventIntense, context)
            "Cuti" -> resolveColorAttribute(R.attr.colorBackgroundUtilityCutiIntense, context)
            "Kerja Khusus" -> resolveColorAttribute(R.attr.colorBackgroundUtilityKerjaKhususIntense, context)
            "Libur" -> resolveColorAttribute(R.attr.colorBackgroundUtilityLiburIntense, context)
            "Ulang Tahun" -> resolveColorAttribute(R.attr.colorBackgroundUtilityUlangTahunIntense, context)
            else -> resolveColorAttribute(R.attr.colorBackgroundPrimaryInverse, context)
        }
    }

    private fun resolveColorAttribute(colorRes: Int, context: android.content.Context): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(colorRes, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            try {
                ContextCompat.getColor(context, colorRes)
            } catch (e: Exception) {
                colorRes
            }
        }
    }

    inner class ChipViewHolder(private val binding: ItemChipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chipText: String, isSelected: Boolean, position: Int) {
            binding.chip.apply {
                this.chipText = chipText

                val backgroundColor = getChipBackgroundColor(chipText, context)
                setActiveBackgroundColor(backgroundColor)

                chipState = if (isSelected) Chip.ChipState.ACTIVE else Chip.ChipState.INACTIVE

                setOnClickListener {
                    if (!isSelected && position != selectedPosition) {
                        updateSelectedPosition(position)
                        onChipClick(position, chipText)
                    }
                }
                isClickable = !isSelected
            }
        }
    }
}