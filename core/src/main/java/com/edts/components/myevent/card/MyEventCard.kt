package com.edts.components.myevent.card

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.MyEventCardBinding
import com.edts.components.event.card.EventCardBadge
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

class MyEventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: MyEventCardBinding =
        MyEventCardBinding.inflate(LayoutInflater.from(context), this, true)

    var myEventCardDelegate: MyEventCardDelegate? = null

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
        setupCardAppearance()
        applyStyledAttributes(attrs)
        isClickable = true
        isFocusable = true
    }

    private fun applyStyledAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.MyEventCard, 0, 0) {
            eventType = getString(R.styleable.MyEventCard_myEventType)
            eventTitle = getString(R.styleable.MyEventCard_myEventTitle)
            eventTime = getString(R.styleable.MyEventCard_myEventTime)
            binding.customCalendarCard.month = getString(R.styleable.MyEventCard_month)
            binding.customCalendarCard.date = getString(R.styleable.MyEventCard_date)
            binding.customCalendarCard.day = getString(R.styleable.MyEventCard_day)
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)
        val cornerRadius = resources.getDimension(R.dimen.radius_8dp)
        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        val elevation = resources.getDimension(R.dimen.dimen_1dp)

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = MaterialColors.getColor(
                context,
                R.attr.colorForegroundPrimary,
                Color.BLACK
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        Log.d("MyEventCard", "MyEventCard Clicked ✅")
        myEventCardDelegate?.onClick(this)
        return true
    }

    fun setCalendarData(month: String, date: String, day: String) {
        binding.customCalendarCard.setCalendarData(month, date, day)
    }

    /**
     * Programmatically sets the properties of the status badge.
     * This makes the card a "dumb" component that just displays what it's given.
     */
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
}