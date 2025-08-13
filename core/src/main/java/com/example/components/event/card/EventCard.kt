package com.example.components.event.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
//import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.EventCardBinding
import com.google.android.material.card.MaterialCardView

class EventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventCardBinding

    // Properties for event image
    var eventImageSrc: Int? = null
        set(value) {
            field = value
            updateEventImage()
        }

    // Properties for badge - using EventCardBadge enums
    var badgeType: EventCardBadge.BadgeType = EventCardBadge.BadgeType.LIVE
        set(value) {
            field = value
            updateBadge()
        }

    var badgeText: String? = null
        set(value) {
            field = value
            updateBadge()
        }

    // Properties for banner
    var eventType: String? = null
        set(value) {
            field = value
            updateBanner()
        }

    var eventCategory: String? = null
        set(value) {
            field = value
            updateBanner()
        }

    // Properties for description
    var eventTitle: String? = null
        set(value) {
            field = value
            updateDescription()
        }

    var eventDate: String? = null
        set(value) {
            field = value
            updateDescription()
        }

    // Properties for status - using EventCardStatus enums
    var statusType: EventCardStatus.StatusType = EventCardStatus.StatusType.UNREGISTERED
        set(value) {
            field = value
            updateStatus()
        }

    var statusText: String? = null
        set(value) {
            field = value
            updateStatus()
        }

    init {
        binding = EventCardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        // Parse custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCard,
            0, 0
        ).apply {
            try {
                // Parse event image
                eventImageSrc = getResourceId(R.styleable.EventCard_eventImageSrc, -1)
                    .takeIf { it != -1 }

                // Parse badge attributes
                val badgeTypeValue = getInt(R.styleable.EventCardBadge_badgeType, 0)
                badgeType = EventCardBadge.BadgeType.fromValue(badgeTypeValue)
                badgeText = getString(R.styleable.EventCardBadge_badgeText)

                // Parse banner attributes
                eventType = getString(R.styleable.EventCardBanner_eventType)
                eventCategory = getString(R.styleable.EventCardBanner_eventCategory)

                // Parse description attributes
                eventTitle = getString(R.styleable.EventCardDescription_eventTitle)
                eventDate = getString(R.styleable.EventCardDescription_eventDate)

                // Parse status attributes
                val statusTypeValue = getInt(R.styleable.EventCardStatus_statusType, 0)
                statusType = EventCardStatus.StatusType.fromValue(statusTypeValue)
                statusText = getString(R.styleable.EventCardStatus_statusText)

                // Update all components
                updateEventImage()
                updateBadge()
                updateBanner()
                updateDescription()
                updateStatus()

            } finally {
                recycle()
            }
        }
    }

    private fun updateEventImage() {
        eventImageSrc?.let { imageRes ->
            binding.ivEventCard.setImageResource(imageRes)
        }
    }

    private fun updateBadge() {
        // Update badge using the existing EventCardBadge custom view
        binding.cvEventCardBadge?.let { badge ->
            badge.badgeType = badgeType
            badgeText?.let { text ->
                badge.badgeText = text
            }
        }
    }

    private fun updateBanner() {
        // Update banner using the existing EventCardBanner custom view
        binding.cvEventCardBanner?.let { banner ->
            eventType?.let { type ->
                banner.eventType = type
            }
            eventCategory?.let { category ->
                banner.eventCategory = category
            }
        }
    }

    private fun updateDescription() {
        // Update description using the existing EventCardDescription custom view
        binding.cvEventCardDescription?.let { description ->
            eventTitle?.let { title ->
                description.eventTitle = title
            }
            eventDate?.let { date ->
                description.eventDate = date
            }
        }
    }

    private fun updateStatus() {
        // Update status using the existing EventCardStatus custom view
        binding.cvEventCardStatus?.let { status ->
            status.statusType = statusType
            statusText?.let { text ->
                status.statusText = text
            }
        }
    }
}