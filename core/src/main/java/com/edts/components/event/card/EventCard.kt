package com.edts.components.event.card

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.EventCardBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class EventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventCardBinding = EventCardBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val cornerRadiusPx = 8.dpToPx.toFloat()

    var eventImageSrc: Int? = null
        set(value) {
            field = value
            updateEventImage()
        }

    var showBadge: Boolean = false
        set(value) {
            field = value
            updateBadgeVisibility()
        }

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

    var showStatus: Boolean = true
        set(value) {
            field = value
            updateStatusVisibility()
        }

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

    enum class CardState {
        REST,
        ON_PRESS
    }

    private var cardState: CardState = CardState.REST
        set(value) {
            field = value
        }

    var eventCardDelegate: EventCardDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    private companion object {
        const val TAG = "EventCard"
    }

    init {
        radius = cornerRadiusPx

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCard,
            0, 0
        ).apply {
            try {ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))

                eventImageSrc = getResourceId(R.styleable.EventCard_eventImageSrc, -1)
                    .takeIf { it != -1 }

                showBadge = getBoolean(R.styleable.EventCard_showCardBadge, true)

                val badgeTypeValue = getInt(R.styleable.EventCard_cardBadgeType, 0)
                badgeType = EventCardBadge.BadgeType.fromValue(badgeTypeValue)
                badgeText = getString(R.styleable.EventCard_cardBadgeText)

                eventType = getString(R.styleable.EventCard_cardEventType)
                eventCategory = getString(R.styleable.EventCard_cardEventCategory)

                eventTitle = getString(R.styleable.EventCard_cardEventTitle)
                eventDate = getString(R.styleable.EventCard_cardEventDate)

                val statusTypeValue = getInt(R.styleable.EventCard_cardStatusType, 0)
                showStatus = getBoolean(R.styleable.EventCard_showCardStatus, true)
                statusType = EventCardStatus.StatusType.fromValue(statusTypeValue)
                statusText = getString(R.styleable.EventCard_cardStatusText)

                updateEventImage()
                updateBadgeVisibility()
                updateBadge()
                updateBanner()
                updateDescription()
                updateStatus()
                setupCardPressState()
                updateStatusVisibility()

                setOnClickListener {
                    handleClick()
                }
            } finally {
                recycle()
            }
        }
    }

    private fun updateStatusVisibility() {
        binding.cvEventCardStatus.visibility = if (showStatus) View.VISIBLE else View.INVISIBLE
    }

    private fun handleClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            Log.d(TAG, "EventCard clicked!")
            Log.d(TAG, "  - Event Title: ${eventTitle ?: "No title"}")
            Log.d(TAG, "  - Event Type: ${eventType ?: "No type"}")
            Log.d(TAG, "  - Event Category: ${eventCategory ?: "No category"}")
            Log.d(TAG, "  - Event Date: ${eventDate ?: "No date"}")
            Log.d(TAG, "  - Badge Type: $badgeType")
            Log.d(TAG, "  - Badge Text: ${badgeText ?: "No badge text"}")
            Log.d(TAG, "  - Status Type: $statusType")
            Log.d(TAG, "  - Status Text: ${statusText ?: "No status text"}")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Total system clicks: $clickCount")
            Log.d(TAG, "--------------------")

            eventCardDelegate?.onEventCardClick(this)
        } else {
            Log.d(TAG, "Click ignored due to debounce (too fast)")
        }
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
    }

    private fun updateEventImage() {
        eventImageSrc?.let { imageRes ->
            binding.ivEventCard.setImageResource(imageRes)
        }
    }

    private fun updateBadgeVisibility() {
        binding.cvEventCardBadge?.visibility = if (showBadge) View.VISIBLE else View.GONE
    }

    private fun updateBadge() {
        binding.cvEventCardBadge?.let { badge ->
            badge.badgeType = badgeType
            badgeText?.let { text ->
                badge.badgeText = text
            }
        }
    }

    private fun updateBanner() {
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
        binding.cvEventCardStatus?.let { status ->
            status.statusType = statusType
            statusText?.let { text ->
                status.statusText = text
            }
        }
    }

    fun resetClickCount() {
        val previousCount = clickCount
        clickCount = 0
        Log.d(TAG, "Click count reset from $previousCount to 0")
    }

    fun getClickCount(): Int {
        return clickCount
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "Programmatic click triggered")
        handleClick()
        return super.performClick()
    }
}