package com.edts.desklabv3.features.leave.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.leave.card.LeaveQuotaCard
import com.edts.desklabv3.features.event.model.LeaveQuota

class EmployeeLeaveQuotaAdapter(private val items: List<LeaveQuota>) :
    RecyclerView.Adapter<EmployeeLeaveQuotaAdapter.LeaveQuotaViewHolder>() {
    class LeaveQuotaViewHolder(val card: LeaveQuotaCard) :
        RecyclerView.ViewHolder(card) {

        fun bind(item: LeaveQuota) {
            card.apply {
                title = item.title
                leaveQuota = item.quota
                expiredDate = item.expiredDate
                leaveUsed = item.used
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveQuotaViewHolder {
        val card = LeaveQuotaCard(parent.context)

        card.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return LeaveQuotaViewHolder(card)
    }
    override fun onBindViewHolder(holder: LeaveQuotaViewHolder, position: Int) {
        holder.bind(items[position])
    }
    override fun getItemCount(): Int = items.size
}