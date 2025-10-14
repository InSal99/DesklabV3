package com.edts.desklabv3.features.event.ui.invitation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.notification.EventNotificationCard
import com.edts.components.notification.EventNotificationCardDelegate
import com.edts.desklabv3.features.event.model.EventInvitation

class EventInvitationAdapter(
    private val notifications: List<EventInvitation>,
    private val onCardClick: (EventInvitation) -> Unit,
    private val onButtonClick: (EventInvitation) -> Unit
) : RecyclerView.Adapter<EventInvitationAdapter.NotificationViewHolder>() {
    class NotificationViewHolder(val card: EventNotificationCard) : RecyclerView.ViewHolder(card) {

        fun bind(
            item: EventInvitation,
            onCardClick: (EventInvitation) -> Unit,
            onButtonClick: (EventInvitation) -> Unit
        ) {
            card.apply {
                title = item.title
                description = item.description
                buttonText = item.buttonText
                isButtonVisible = item.isButtonVisible
                eventCategory = item.eventCategory
                eventNotificationCardDelegate = object : EventNotificationCardDelegate {
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
        val card = EventNotificationCard(parent.context)

        card.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return NotificationViewHolder(card)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position], onCardClick, onButtonClick)
    }

    override fun getItemCount(): Int = notifications.size
}