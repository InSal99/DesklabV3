package com.example.desklabv3.features.event.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.components.notification.EventNotificationCard
import com.example.components.notification.EventNotificationCardDelegate
import com.example.desklabv3.databinding.ItemEventInvitationBinding
import com.example.desklabv3.features.event.model.EventInvitation

class EventInvitationAdapter(
    private val notifications: List<EventInvitation>,
    private val onCardClick: (EventInvitation) -> Unit,
    private val onButtonClick: (EventInvitation) -> Unit
) : RecyclerView.Adapter<EventInvitationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(val binding: ItemEventInvitationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: EventInvitation,
            onCardClick: (EventInvitation) -> Unit,
            onButtonClick: (EventInvitation) -> Unit
        ) {
            binding.cvNotificationCard.apply {
                title = item.title
                description = item.description
                buttonText = item.buttonText
                isButtonVisible = item.isButtonVisible
                eventType = item.eventType

                delegate = object : EventNotificationCardDelegate {
                    override fun onCardClick(notificationCard: EventNotificationCard) {
                        onCardClick(item)
                    }

                    override fun onButtonClick(notificationCard: EventNotificationCard) {
                        onButtonClick(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemEventInvitationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position], onCardClick, onButtonClick)
    }

    override fun getItemCount(): Int = notifications.size
}