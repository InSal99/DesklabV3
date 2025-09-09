package com.edts.desklabv3.features.event.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.components.option.card.OptionCard

class EventOptionAdapter(
    private val onOptionClick: (Int) -> Unit
) : RecyclerView.Adapter<EventOptionAdapter.OptionViewHolder>() {

    private var options = emptyList<Pair<String, Int>>()

    class OptionViewHolder(private val optionCard: OptionCard) : RecyclerView.ViewHolder(optionCard) {
        fun bind(option: Pair<String, Int>, position: Int, onOptionClick: (Int) -> Unit) {
            optionCard.apply {
                titleText = option.first
                iconResource = option.second

                delegate = object : com.edts.components.option.card.OptionCardDelegate {
                    override fun onClick(card: OptionCard) {
                        onOptionClick(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val optionCard = OptionCard(parent.context)

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            val margin = parent.context.resources.getDimensionPixelSize(R.dimen.margin_8dp)
            setMargins(0, margin, 0, margin)
        }
        optionCard.layoutParams = layoutParams

        return OptionViewHolder(optionCard)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position], position, onOptionClick)
    }

    override fun getItemCount(): Int = options.size

    fun submitList(newOptions: List<Pair<String, Int>>) {
        options = newOptions
        notifyDataSetChanged()
    }
}