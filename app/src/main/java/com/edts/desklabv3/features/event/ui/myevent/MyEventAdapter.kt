package com.edts.desklabv3.features.event.ui.myevent

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.myevent.card.MyEventCard
import com.edts.components.myevent.card.MyEventCardDelegate
import com.edts.desklabv3.features.event.model.MyEvent

class MyEventAdapter(
    private val onItemClick: (MyEvent) -> Unit
) : ListAdapter<MyEvent, MyEventAdapter.EventViewHolder>(MyEventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val myEventCard = MyEventCard(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        }
        return EventViewHolder(myEventCard)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class EventViewHolder(private val myEventCard: MyEventCard) :
        RecyclerView.ViewHolder(myEventCard) {
        fun bind(event: MyEvent, onItemClick: (MyEvent) -> Unit) {
            myEventCard.apply {
                myEventType = event.myEventType
                eventLocation = event.myEventLocation
                eventTitle = event.title
                eventTime = event.time
                setCalendarData(month = event.month, date = event.date, day = event.day)
                setBadgeData(
                    text = event.badgeText,
                    type = event.badgeType,
                    size = event.badgeSize,
                    isVisible = event.isBadgeVisible
                )
                myEventCardDelegate = object : MyEventCardDelegate {
                    override fun onClick(eventCard: MyEventCard) {
                        onItemClick(event)
                    }
                }
            }
        }
    }

    private class MyEventDiffCallback : DiffUtil.ItemCallback<MyEvent>() {
        override fun areItemsTheSame(oldItem: MyEvent, newItem: MyEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyEvent, newItem: MyEvent): Boolean {
            return oldItem == newItem
        }
    }
}