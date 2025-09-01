package com.edts.desklabv3.features.event.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.databinding.DetailInformationABinding

class EventTimeLocationAdapter : RecyclerView.Adapter<EventTimeLocationAdapter.TimeLocationViewHolder>() {

    private val items = mutableListOf<Triple<Int, String, String>>() // Triple<IconRes, PrimaryText, SecondaryText>

    inner class TimeLocationViewHolder(private val binding: DetailInformationABinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Triple<Int, String, String>) {
            binding.ivInfoIcon.setImageResource(item.first) // Icon resource
            binding.tvInfoTitle.text = item.second   // Primary text
            binding.tvDetailDescription.text = item.third  // Secondary text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLocationViewHolder {
        val binding = DetailInformationABinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeLocationViewHolder(binding)
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