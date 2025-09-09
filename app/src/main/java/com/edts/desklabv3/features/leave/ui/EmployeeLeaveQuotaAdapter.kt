package com.edts.desklabv3.features.leave.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.leave.card.LeaveQuotaCard
import com.edts.desklabv3.features.event.model.LeaveQuota

class EmployeeLeaveQuotaAdapter(private val items: List<LeaveQuota>) :
    RecyclerView.Adapter<EmployeeLeaveQuotaAdapter.LeaveQuotaViewHolder>() {

    /**
     * ViewHolder holds the view for a single item using ViewBinding.
     */
    class LeaveQuotaViewHolder(val card: LeaveQuotaCard) :
        RecyclerView.ViewHolder(card) {

        fun bind(item: LeaveQuota) {
            // Use the updated property-based API of the custom card.
            // This is cleaner than calling setter methods.
            card.apply {
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
        val card = LeaveQuotaCard(parent.context)

        card.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return LeaveQuotaViewHolder(card)
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