package com.edts.desklabv3.features.event.ui.eventlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.event.card.EventCardBadge
import com.edts.components.event.card.EventCardStatus
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.ItemEventBinding
import com.edts.desklabv3.features.event.model.Event
import com.edts.desklabv3.features.event.model.EventSample
import com.edts.desklabv3.features.event.model.EventCategory
import com.edts.desklabv3.features.event.model.EventStatus
import com.edts.desklabv3.features.event.model.EventType
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter(
    private var events: List<EventSample>,
    private val onEventClick: (EventSample) -> Unit
) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<EventSample>) {
        events = newEvents
        notifyDataSetChanged()
    }

    private fun getCategoryDisplayName(category: EventCategory): String {
        return when (category) {
            EventCategory.EMPLOYEE_BENEFIT -> "Employee Benefit"
            EventCategory.GENERAL_EVENT -> "General Event"
            EventCategory.PEOPLE_DEVELOPMENT -> "People Development"
        }
    }

    private fun getEventTypeDisplayName(eventType: EventType): String {
        return when (eventType) {
            is EventType.Online -> "Online Event"
            is EventType.Offline -> "Offline Event"
            is EventType.Hybrid -> "Hybrid Event"
        }
    }

    private fun getImageResourceName(imageName: String): Int {
        return when (imageName) {
            "image_event_power" -> R.drawable.image_event_power
            "image_event_game" -> R.drawable.image_event_game
            "image_event_simplyfying" -> R.drawable.image_event_simplyfying
            "image_event_it" -> R.drawable.image_event_it
            else -> 0
        }
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventSample) {
            binding.eventCard.apply {
                // Set basic event info
                eventTitle = event.eventTitle
                eventDate = dateFormatter.format(event.eventDate)
                eventCategory = getCategoryDisplayName(event.eventCategory)
                eventType = getEventTypeDisplayName(event.eventType)
                eventImageSrc = getImageResourceName(event.eventImage)

                // Handle badge display
                event.badgeType?.let { badgeType ->
                    showBadge = true
                    this.badgeType = badgeType
                    badgeText = event.badgeText ?: ""
                } ?: run {
                    showBadge = false
                }

                // Handle status display
                event.statusType?.let { statusType ->
                    this.statusType = statusType
                    statusText = event.statusText ?: ""
                }

                // Set click listener
                setOnClickListener {
                    onEventClick(event)
                }
            }
        }
    }
}