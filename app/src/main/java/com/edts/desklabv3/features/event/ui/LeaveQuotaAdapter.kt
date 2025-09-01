package com.edts.desklabv3.features.event.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.desklabv3.databinding.ItemLeaveQuotaBinding
import com.edts.desklabv3.features.event.model.LeaveQuota

class LeaveQuotaAdapter(private val items: List<LeaveQuota>) :
    RecyclerView.Adapter<LeaveQuotaAdapter.LeaveQuotaViewHolder>() {

    /**
     * ViewHolder holds the view for a single item using ViewBinding.
     */
    class LeaveQuotaViewHolder(private val binding: ItemLeaveQuotaBinding) :
        RecyclerView.ViewHolder(binding.root as View) {

        fun bind(item: LeaveQuota) {
            // Use the updated property-based API of the custom card.
            // This is cleaner than calling setter methods.
            binding.leaveQuotaCardItem.apply {
                title = item.title
                leaveQuota = item.quota
                expiredDate = item.expiredDate
                leaveUsed = item.used
            }
        }
    }

    /**
     * Creates a new ViewHolder by inflating the item layout using ViewBinding.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveQuotaViewHolder {
        val binding = ItemLeaveQuotaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LeaveQuotaViewHolder(binding)
    }

    /**
     * Binds the data from our list to the views in the ViewHolder.
     */
    override fun onBindViewHolder(holder: LeaveQuotaViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /**
     * Returns the total number of items in the list.
     */
    override fun getItemCount(): Int = items.size
}