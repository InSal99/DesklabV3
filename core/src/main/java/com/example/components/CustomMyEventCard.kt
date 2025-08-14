package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.databinding.CustomMyEventCardBinding
import com.example.components.event.card.EventCardBadge


class CustomMyEventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomMyEventCardBinding

    var eventType: String? = null
        set(value) {
            field = value
            binding.tvEventType.text = value
        }

    var eventTitle: String? = null
        set(value) {
            field = value
            binding.tvEventTitle.text = value
        }

    var eventTime: String? = null
        set(value) {
            field = value
            binding.tvEventTime.text = value
        }

    init {
        binding = CustomMyEventCardBinding.inflate(LayoutInflater.from(context), this, true)

        attrs?.let {
            // Attributes are obtained using the parent's styleable
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomMyEventCard, 0, 0)
            try {
                // Set properties for CustomMyEventCard itself
                eventType = typedArray.getString(R.styleable.CustomMyEventCard_myEventType)
                eventTitle = typedArray.getString(R.styleable.CustomMyEventCard_myEventTitle)
                eventTime = typedArray.getString(R.styleable.CustomMyEventCard_myEventTime)

                // --- DELEGATE ATTRIBUTES TO CHILDREN ---

                // Pass calendar attributes directly to the CustomCalendarCard instance
                binding.customCalendarCard.month = typedArray.getString(R.styleable.CustomMyEventCard_month)
                binding.customCalendarCard.date = typedArray.getString(R.styleable.CustomMyEventCard_date)
                binding.customCalendarCard.day = typedArray.getString(R.styleable.CustomMyEventCard_day)

                // Pass badge attributes directly to the EventCardBadge instance
                val badgeTypeValue = typedArray.getInt(R.styleable.CustomMyEventCard_badgeType, 0)
                binding.eventCardBadge.badgeType = EventCardBadge.BadgeType.fromValue(badgeTypeValue)
                binding.eventCardBadge.badgeText = typedArray.getString(R.styleable.CustomMyEventCard_badgeText)

            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Programmatically sets the calendar data.
     */
    fun setCalendarData(month: String, date: String, day: String) {
        binding.customCalendarCard.setCalendarData(month, date, day)
    }

    /**
     * Programmatically sets the badge data.
     */
    fun setBadgeData(badgeType: EventCardBadge.BadgeType, badgeText: String?) {
        binding.eventCardBadge.badgeType = badgeType
        binding.eventCardBadge.badgeText = badgeText
    }
}