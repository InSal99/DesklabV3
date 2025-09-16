package com.edts.desklabv3.features.leave.ui.laporantim

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.card.detail.CardDetailInfoB
import com.edts.components.radiobutton.RadioButton

class FilterAdapter(
    private val items: List<Pair<String, String>>,
    private val onClick: (CardDetailInfoB, Int) -> Unit,
    private var selectedPosition: Int = RecyclerView.NO_POSITION
) : RecyclerView.Adapter<FilterAdapter.CardDetailInfoBViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDetailInfoBViewHolder {
        val card = CardDetailInfoB(parent.context)

        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        card.layoutParams = layoutParams

        val radio = RadioButton(parent.context).apply {
            isClickable = false
            isFocusable = false
        }
        card.setRightSlotView(radio)

        return CardDetailInfoBViewHolder(card, radio)
    }

    override fun onBindViewHolder(holder: CardDetailInfoBViewHolder, position: Int) {
        val (title, desc) = items[position]
        holder.card.titleText = title
        holder.card.descText = desc
        holder.card.showLeftSlot = false
        holder.card.showIndicator = false
        holder.radio.isChecked = position == selectedPosition

        holder.card.setOnClickListener {
            updateSelection(holder.adapterPosition)
            onClick(holder.card, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = items.size

    fun getSelectedPosition(): Int = selectedPosition

    fun getSelectedItem(): Pair<String, String>? =
        if (selectedPosition != RecyclerView.NO_POSITION) items[selectedPosition] else null

    fun setSelectedPosition(position: Int) {
        val oldPos = selectedPosition
        selectedPosition = position

        if (oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos)
        if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition)
    }

    private fun updateSelection(newPos: Int) {
        if (newPos == RecyclerView.NO_POSITION) return

        val oldPos = selectedPosition
        selectedPosition = newPos

        if (oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos)
        notifyItemChanged(selectedPosition)
    }

    class CardDetailInfoBViewHolder(val card: CardDetailInfoB, val radio: RadioButton) :
        RecyclerView.ViewHolder(card)
}






//class WeekFilterAdapter(
//    private val items: List<Pair<String, String>>,
//    private val onClick: (CardDetailInfoB, Int) -> Unit,
//    private var selectedPosition: Int = RecyclerView.NO_POSITION
//) : RecyclerView.Adapter<WeekFilterAdapter.CardDetailInfoBViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDetailInfoBViewHolder {
//        val card = CardDetailInfoB(parent.context)
//
//        val layoutParams = RecyclerView.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        card.layoutParams = layoutParams
//
//        val radio = RadioButton(parent.context).apply {
//            isClickable = false
//            isFocusable = false
//        }
//        card.setRightSlotView(radio)
//
//        return CardDetailInfoBViewHolder(card, radio)
//    }
//
//    override fun onBindViewHolder(holder: CardDetailInfoBViewHolder, position: Int) {
//        val (title, desc) = items[position]
//        holder.card.titleText = title
//        holder.card.descText = desc
//        holder.card.showLeftSlot = false
//        holder.card.showIndicator = false
//        holder.radio.isChecked = position == selectedPosition
//
//        holder.card.setOnClickListener {
//            updateSelection(holder.adapterPosition)
//            onClick(holder.card, holder.adapterPosition)
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    fun getSelectedPosition(): Int = selectedPosition
//
//    fun getSelectedItem(): Pair<String, String>? =
//        if (selectedPosition != RecyclerView.NO_POSITION) items[selectedPosition] else null
//
//    fun setSelectedPosition(position: Int) {
//        val oldPos = selectedPosition
//        selectedPosition = position
//
//        if (oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos)
//        if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition)
//    }
//
//    private fun updateSelection(newPos: Int) {
//        if (newPos == RecyclerView.NO_POSITION) return
//
//        val oldPos = selectedPosition
//        selectedPosition = newPos
//
//        if (oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos)
//        notifyItemChanged(selectedPosition)
//    }
//
//    class CardDetailInfoBViewHolder(val card: CardDetailInfoB, val radio: RadioButton) :
//        RecyclerView.ViewHolder(card)
//}