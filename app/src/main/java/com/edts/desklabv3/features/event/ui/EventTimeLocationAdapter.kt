package com.edts.desklabv3.features.event.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.detail.information.DetailInformationA

class EventTimeLocationAdapter : RecyclerView.Adapter<EventTimeLocationAdapter.TimeLocationViewHolder>() {

    private val items = mutableListOf<Triple<Int, String, String>>()

    inner class TimeLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val detailInfoView = itemView as DetailInformationA

        fun bind(item: Triple<Int, String, String>) {
            detailInfoView.apply {
                icon = ContextCompat.getDrawable(context, item.first)
                title = item.second
                description = item.third
                hasAction = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLocationViewHolder {
        val detailInfoView = DetailInformationA(parent.context)
        detailInfoView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return TimeLocationViewHolder(detailInfoView)
    }

    override fun onBindViewHolder(holder: TimeLocationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Triple<Int, String, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}