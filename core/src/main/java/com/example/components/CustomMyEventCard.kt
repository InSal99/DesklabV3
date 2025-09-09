package com.example.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.components.databinding.CustomMyEventCardBinding
import com.example.components.event.card.EventCardBadge
import com.google.android.material.color.MaterialColors

/**
 * A composite custom view that displays event details in a styled card.
 *
 * This component follows the composite view pattern by extending `FrameLayout`. It inflates
 * its own layout (`custom_my_event_card.xml`), which contains a `MaterialCardView`
 * and other child views. This encapsulates the complex layout and provides a simple,
 * reusable component with a clean API.
 *
 * ### XML Usage Example:
 * Declare the `CustomMyEventCard` in your layout and configure its properties and the
 * properties of its children through custom attributes.
 *
 * ```xml
 * <com.example.components.CustomMyEventCard
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * app:myEventType="Online Event"
 * app:myEventTitle="Game Night: Mobile Legend Tournament"
 * app:myEventTime="18:00 - 20:00 WIB"
 * app:month="JUL"
 * app:date="24"
 * app:day="Wed"
 * app:badgeText="Registered"
 * app:badgeBackgroundColor="?attr/colorBackgroundSuccessIntense"
 * app:badgeTextColor="?attr/colorOnPrimary"
 * app:badgeVisible="true" />
 * ```
 *
 * ### Programmatic Usage Example:
 *
 * ```kotlin
 * val myEventCard = CustomMyEventCard(context).apply {
 * eventType = "Online Event"
 * eventTitle = "Team Sync-Up"
 * eventTime = "10:00 - 11:00 WIB"
 * setCalendarData("AUG", "18", "Mon")
 * setBadgeData(
 * text = "Confirmed",
 * backgroundColor = ContextCompat.getColor(context, R.color.green),
 * textColor = Color.WHITE,
 * isVisible = true
 * )
 * // Set the delegate to handle clicks
 * customMyEventCardDelegate = object : CustomMyEventCardDelegate {
 * override fun onClick(eventCard: CustomMyEventCard) {
 * // Handle card click event
 * }
 * }
 * }
 * parentLayout.addView(myEventCard)
 * ```
 */
class CustomMyEventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomMyEventCardBinding

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
        // Inflate the component's layout and attach it to this FrameLayout.
        binding = CustomMyEventCardBinding.inflate(LayoutInflater.from(context), this, true)

        // Make the entire view clickable to trigger performClick()
        isClickable = true
        isFocusable = true

        // Parse attributes from the XML layout.
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

                // Pass badge attributes to the child EventCardBadge.
//                val badgeText = typedArray.getString(R.styleable.CustomMyEventCard_eventCardBadgeText)
                val badgeTypeOrdinal = typedArray.getInt(R.styleable.CustomMyEventCard_myEventType, 1) // Default to INVITED
                val badgeSizeOrdinal = typedArray.getInt(R.styleable.EventCardBadge_badgeSize, 0) // Default to SMALL
//                val badgeVisible = typedArray.getBoolean(R.styleable.CustomMyEventCard_badgeVisible, true)

//                setBadgeData(badgeText, badgeTypeOrdinal, badgeSizeOrdinal, badgeVisible)

            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Performs the click action and notifies the delegate.
     * This is called automatically when the view is clicked because `isClickable` is true.
     */
    override fun performClick(): Boolean {
        super.performClick()
        Log.d("CustomMyEventCard", "MyEventCard Clicked ✅")
        customMyEventCardDelegate?.onClick(this)
        return true
    }

    /**
     * Programmatically sets the date on the child `CustomCalendarCard`.
     * @param month The three-letter month (e.g., "AUG").
     * @param date The numeric day (e.g., "18").
     * @param day The three-letter day of the week (e.g., "Mon").
     */
    fun setCalendarData(month: String, date: String, day: String) {
        binding.customCalendarCard.setCalendarData(month, date, day)
    }

    /**
     * Programmatically sets the properties of the EventCardBadge.
     * @param text The text to display on the badge. Can be null to hide text.
     * @param badgeType The badge type enum value (0=live, 1=invited, 2=registered).
     * @param badgeSize The badge size enum value (0=Small, 1=Large).
     * @param isVisible Whether the badge should be visible.
     */
    fun setBadgeData(text: String?, badgeType: Int, badgeSize: Int, isVisible: Boolean) {
        // Set the text
        binding.eventCardBadge.badgeText = text

        // Set badge type using the enum
        binding.eventCardBadge.badgeType = EventCardBadge.BadgeType.fromValue(badgeType)

        // Set badge size using the enum
        binding.eventCardBadge.badgeSize = EventCardBadge.BadgeSize.fromValue(badgeSize)

        // Set visibility
        binding.eventCardBadge.isVisible = isVisible
    }

    /**
     * Alternative method with enum types for better type safety
     */
    fun setBadgeData(text: String?, badgeType: EventCardBadge.BadgeType, badgeSize: EventCardBadge.BadgeSize, isVisible: Boolean) {
        binding.eventCardBadge.badgeText = text
        binding.eventCardBadge.badgeType = badgeType
        binding.eventCardBadge.badgeSize = badgeSize
        binding.eventCardBadge.isVisible = isVisible
    }

    /**
     * Simplified method for common use cases
     */
    fun setBadgeData(text: String?, badgeType: EventCardBadge.BadgeType) {
        setBadgeData(text, badgeType, EventCardBadge.BadgeSize.SMALL, true)
    }

    /**
     * Convenience method to set badge using string values (useful for data binding)
     */
    fun setBadgeData(text: String?, badgeTypeString: String, badgeSizeString: String, isVisible: Boolean) {
        val badgeType = when (badgeTypeString.lowercase()) {
            "live" -> EventCardBadge.BadgeType.LIVE
            "invited" -> EventCardBadge.BadgeType.INVITED
            "registered" -> EventCardBadge.BadgeType.REGISTERED
            else -> EventCardBadge.BadgeType.INVITED
        }

        val badgeSize = when (badgeSizeString.lowercase()) {
            "small" -> EventCardBadge.BadgeSize.SMALL
            "large" -> EventCardBadge.BadgeSize.LARGE
            else -> EventCardBadge.BadgeSize.SMALL
        }

        setBadgeData(text, badgeType, badgeSize, isVisible)
    }
}

//class CustomMyEventCard @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    private val binding: CustomMyEventCardBinding
//
//    /**
//     * The delegate responsible for handling click events on this card.
//     * Assign an object that implements [CustomMyEventCardDelegate] to receive callbacks.
//     */
//    var customMyEventCardDelegate: CustomMyEventCardDelegate? = null
//
//    /** The type or category of the event (e.g., "Online Event"). */
//    var eventType: String? = null
//        set(value) {
//            field = value
//            binding.tvEventType.text = value
//        }
//
//    /** The main title of the event. */
//    var eventTitle: String? = null
//        set(value) {
//            field = value
//            binding.tvEventTitle.text = value
//        }
//
//    /** The time of the event (e.g., "18:00 - 20:00 WIB"). */
//    var eventTime: String? = null
//        set(value) {
//            field = value
//            binding.tvEventTime.text = value
//        }
//
//    init {
//        // Inflate the component's layout and attach it to this FrameLayout.
//        binding = CustomMyEventCardBinding.inflate(LayoutInflater.from(context), this, true)
//
//        // Make the entire view clickable to trigger performClick()
//        isClickable = true
//        isFocusable = true
//
//        // Parse attributes from the XML layout.
//        attrs?.let {
//            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomMyEventCard, 0, 0)
//            try {
//                // Set properties for CustomMyEventCard itself.
//                eventType = typedArray.getString(R.styleable.CustomMyEventCard_myEventType)
//                eventTitle = typedArray.getString(R.styleable.CustomMyEventCard_myEventTitle)
//                eventTime = typedArray.getString(R.styleable.CustomMyEventCard_myEventTime)
//
//                // Pass calendar attributes directly to the child CustomCalendarCard.
//                binding.customCalendarCard.month = typedArray.getString(R.styleable.CustomMyEventCard_month)
//                binding.customCalendarCard.date = typedArray.getString(R.styleable.CustomMyEventCard_date)
//                binding.customCalendarCard.day = typedArray.getString(R.styleable.CustomMyEventCard_day)
//
//                // Pass badge attributes to the child MaterialChip.
//                val badgeText = typedArray.getString(R.styleable.CustomMyEventCard_badgeText)
//                val badgeBgColor = typedArray.getColor(
//                    R.styleable.CustomMyEventCard_badgeBackgroundColor,
//                    -1 // Default to no color
//                )
//                val badgeTextColor = typedArray.getColor(
//                    R.styleable.CustomMyEventCard_badgeTextColor,
//                    MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurface)
//                )
//                val badgeVisible = typedArray.getBoolean(R.styleable.CustomMyEventCard_badgeVisible, true)
//
//                setBadgeData(badgeText, badgeBgColor, badgeTextColor, badgeVisible)
//
//            } finally {
//                typedArray.recycle()
//            }
//        }
//    }
//
//    /**
//     * Performs the click action and notifies the delegate.
//     * This is called automatically when the view is clicked because `isClickable` is true.
//     */
//    override fun performClick(): Boolean {
//        super.performClick()
//        Log.d("CustomMyEventCard", "MyEventCard Clicked ✅")
//        customMyEventCardDelegate?.onClick(this)
//        return true
//    }
//
//
//    /**
//     * Programmatically sets the date on the child `CustomCalendarCard`.
//     * @param month The three-letter month (e.g., "AUG").
//     * @param date The numeric day (e.g., "18").
//     * @param day The three-letter day of the week (e.g., "Mon").
//     */
//    fun setCalendarData(month: String, date: String, day: String) {
//        binding.customCalendarCard.setCalendarData(month, date, day)
//    }
//
//    /**
//     * Programmatically sets the properties of the status badge.
//     * @param text The text to display on the badge. Can be null to hide text.
//     * @param backgroundColor The resolved color for the badge's background. If -1, the default is used.
//     * @param textColor The resolved color for the badge's text.
//     * @param isVisible Whether the badge should be visible.
//     */
//    fun setBadgeData(text: String?, backgroundColor: Int, textColor: Int, isVisible: Boolean) {
//        binding.eventCardBadge.text = text
//        binding.eventCardBadge.setTextColor(textColor)
//        if (backgroundColor != -1) {
//            binding.eventCardBadge.chipBackgroundColor = ColorStateList.valueOf(backgroundColor)
//        }
//        binding.eventCardBadge.isVisible = isVisible
//    }
//}