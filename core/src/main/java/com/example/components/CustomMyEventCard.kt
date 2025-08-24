package com.example.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.example.components.databinding.CustomMyEventCardBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

/**
 * A composite custom view that displays event details in a styled card.
 *
 * This component extends `MaterialCardView` to create a self-contained, reusable UI element.
 * It inflates its content from the `custom_my_event_card.xml` layout, encapsulating
 * the child views and providing a clean API for interacting with the component.
 *
 */
class CustomMyEventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: CustomMyEventCardBinding = CustomMyEventCardBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * The delegate responsible for handling click events on this card.
     * Assign an object that implements [CustomMyEventCardDelegate] to receive callbacks.
     */
    var customMyEventCardDelegate: CustomMyEventCardDelegate? = null

    /** The type or category of the event (e.g., "Online Event"). */
    var eventType: String? = null
        set(value) {
            field = value
            binding.tvEventType.text = value
        }

    /** The main title of the event. */
    var eventTitle: String? = null
        set(value) {
            field = value
            binding.tvEventTitle.text = value
        }

    /** The time of the event (e.g., "18:00 - 20:00 WIB"). */
    var eventTime: String? = null
        set(value) {
            field = value
            binding.tvEventTime.text = value
        }

    init {

        // Apply the base card appearance attributes programmatically.
        setupCardAppearance()

        isClickable = true
        isFocusable = true

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomMyEventCard, 0, 0)
            try {
                // Set properties for CustomMyEventCard itself.
                eventType = typedArray.getString(R.styleable.CustomMyEventCard_myEventType)
                eventTitle = typedArray.getString(R.styleable.CustomMyEventCard_myEventTitle)
                eventTime = typedArray.getString(R.styleable.CustomMyEventCard_myEventTime)

                // Pass calendar attributes directly to the child CustomCalendarCard.
                binding.customCalendarCard.month = typedArray.getString(R.styleable.CustomMyEventCard_month)
                binding.customCalendarCard.date = typedArray.getString(R.styleable.CustomMyEventCard_date)
                binding.customCalendarCard.day = typedArray.getString(R.styleable.CustomMyEventCard_day)

                // Pass badge attributes to the child MaterialChip.
                val badgeText = typedArray.getString(R.styleable.CustomMyEventCard_badgeText)
                val badgeBgColor = typedArray.getColor(
                    R.styleable.CustomMyEventCard_badgeBackgroundColor,
                    -1
                )
                val badgeTextColor = typedArray.getColor(
                    R.styleable.CustomMyEventCard_badgeCustomTextColor,
                    Color.WHITE
                )
                val badgeVisible = typedArray.getBoolean(R.styleable.CustomMyEventCard_badgeVisible, true)

                setBadgeData(badgeText, badgeBgColor, badgeTextColor, badgeVisible)

            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Configures the visual properties of the card, such as elevation, stroke, and corners.
     * This moves the styling from XML into the component class for better encapsulation.
     */
    private fun setupCardAppearance() {
        val strokeSubtleColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)
        val cornerRadius = resources.getDimension(R.dimen.radius_8dp)
        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        val elevation = resources.getDimension(R.dimen.dimen_1dp)

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

        // Set shadow colors only for API 28+
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

    /**
     * Performs the click action and notifies the delegate.
     */
    override fun performClick(): Boolean {
        super.performClick()
        Log.d("CustomMyEventCard", "MyEventCard Clicked ✅")
        customMyEventCardDelegate?.onClick(this)
        return true
    }

    /**
     * Programmatically sets the date on the child `CustomCalendarCard`.
     */
    fun setCalendarData(month: String, date: String, day: String) {
        binding.customCalendarCard.setCalendarData(month, date, day)
    }

    /**
     * Programmatically sets the properties of the status badge.
     */
    fun setBadgeData(text: String?, backgroundColor: Int, textColor: Int, isVisible: Boolean) {
        binding.eventCardBadge.text = text
        binding.eventCardBadge.setTextColor(textColor)
        if (backgroundColor != -1) {
            binding.eventCardBadge.chipBackgroundColor = ColorStateList.valueOf(backgroundColor)
        }
        binding.eventCardBadge.isVisible = isVisible
    }
}