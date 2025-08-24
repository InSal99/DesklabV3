package com.example.desklabv3.features.event.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.components.CustomMyEventCard
import com.example.components.CustomMyEventCardDelegate
import com.example.desklabv3.databinding.ItemMyEventBinding
import com.example.desklabv3.features.event.model.MyEvent

class MyEventAdapter(
    private val events: List<MyEvent>,
    private val onItemClick: (MyEvent) -> Unit
) : RecyclerView.Adapter<MyEventAdapter.EventViewHolder>() {

    class EventViewHolder(val binding: ItemMyEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: MyEvent, onItemClick: (MyEvent) -> Unit) {
            // The myEventCardItem is an instance of your CustomMyEventCard
            binding.myEventCardItem.apply {
                val context = itemView.context

                // 1. Set data using the public properties and methods
                eventType = event.eventType
                eventTitle = event.title
                eventTime = event.time

                setCalendarData(month = event.month, date = event.date, day = event.day)

                // 2. Resolve theme color attributes before passing them to the custom view
                fun resolveThemeColor(@androidx.annotation.AttrRes attrId: Int): Int {
                    val typedValue = android.util.TypedValue()
                    context.theme.resolveAttribute(attrId, typedValue, true)
                    return typedValue.data
                }
                val backgroundColor = resolveThemeColor(event.badgeBackgroundColor)
                val textColor = resolveThemeColor(event.badgeTextColor)

                setBadgeData(
                    text = event.badgeText,
                    backgroundColor = backgroundColor,
                    textColor = textColor,
                    isVisible = event.isBadgeVisible
                )

                // 3. Handle clicks using the component's intended delegate pattern
                customMyEventCardDelegate = object : CustomMyEventCardDelegate {
                    override fun onClick(eventCard: CustomMyEventCard) {
                        // Pass the event data object back to the activity/fragment
                        onItemClick(event)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemMyEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position], onItemClick)
    }
}