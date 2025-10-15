package com.edts.components.myevent.card

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.MyEventCardBinding
import com.edts.components.event.card.EventCardBadge
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class MyEventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var myEventCardDelegate: MyEventCardDelegate? = null

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

    var eventLocation: MyEventLocation = MyEventLocation.OFFLINE
        set(value) {
            field = value
            binding.tvEventLocation.text = value.displayText
        }

    var myEventType: MyEventType = MyEventType.LIVE
        set(value) {
            field = value
            updateBadgeFromEventType()
        }


    private val binding: MyEventCardBinding =
        MyEventCardBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        setupCardAppearance()
        applyStyledAttributes(attrs)
        isClickable = true
        isFocusable = true
    }

    fun setCalendarData(month: String, date: String, day: String) {
        binding.customCalendarCard.setCalendarData(month, date, day)
    }

    fun setBadgeData(
        text: String?,
        type: EventCardBadge.BadgeType,
        size: EventCardBadge.BadgeSize,
        isVisible: Boolean = true
    ) {
        with(binding.eventCardBadge) {
            this.badgeText = text
            this.badgeType = type
            this.badgeSize = size
            this.isVisible = isVisible
        }
    }


    override fun performClick(): Boolean {
        super.performClick()
        myEventCardDelegate?.onClick(this)
        return true
    }


    private fun applyStyledAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.MyEventCard, 0, 0) {
            val eventTypeValue = getInt(R.styleable.MyEventCard_myEventType, 0)
            val eventLocationValue = getInt(R.styleable.MyEventCard_myEventLocation, 0)
            val badgeVisible = getBoolean(R.styleable.MyEventCard_myEventBadgeVisible, true)

            eventTitle = getString(R.styleable.MyEventCard_myEventTitle)
            eventTime = getString(R.styleable.MyEventCard_myEventTime)
            myEventType = MyEventType.fromValue(eventTypeValue)
            eventLocation = MyEventLocation.fromValue(eventLocationValue)

            binding.customCalendarCard.month = getString(R.styleable.MyEventCard_month)
            binding.customCalendarCard.date = getString(R.styleable.MyEventCard_date)
            binding.customCalendarCard.day = getString(R.styleable.MyEventCard_day)
            binding.eventCardBadge.isVisible = badgeVisible
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = context.resolveColorAttribute(
            R.attr.colorStrokeSubtle,
            R.color.colorNeutral30
        )
        val cornerRadius = 8f.dpToPx
        val strokeWidth = 1.dpToPx
        val elevation = 1f.dpToPx

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = context.resolveColorAttribute(
                R.attr.colorForegroundPrimary,
                R.color.color000
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }

    private fun updateBadgeFromEventType() {
        when (myEventType) {
            MyEventType.LIVE -> setBadgeData(
                text = "Berlangsung",
                type = EventCardBadge.BadgeType.LIVE,
                size = EventCardBadge.BadgeSize.SMALL
            )
            MyEventType.REGISTERED -> setBadgeData(
                text = "Terdaftar",
                type = EventCardBadge.BadgeType.REGISTERED,
                size = EventCardBadge.BadgeSize.SMALL
            )
            MyEventType.ATTENDED -> setBadgeData(
                text = "Hadir",
                type = EventCardBadge.BadgeType.ATTENDED,
                size = EventCardBadge.BadgeSize.SMALL
            )
            MyEventType.NOTATTENDED -> setBadgeData(
                text = "Tidak Hadir",
                type = EventCardBadge.BadgeType.NOTATTENDED,
                size = EventCardBadge.BadgeSize.SMALL
            )
        }
    }

    enum class MyEventType(val value: Int) {
        LIVE(0),
        REGISTERED(1),
        ATTENDED(2),
        NOTATTENDED(3);

        companion object {
            fun fromValue(value: Int): MyEventType {
                return values().find { it.value == value } ?: LIVE
            }
        }
    }

    enum class MyEventLocation(val value: Int, val displayText: String) {
        OFFLINE(0, "Offline Event"),
        ONLINE(1, "Online Event"),
        HYBRID(2, "Hybrid Event");

        companion object {
            fun fromValue(value: Int): MyEventLocation {
                return values().find { it.value == value } ?: OFFLINE
            }
        }
    }
}