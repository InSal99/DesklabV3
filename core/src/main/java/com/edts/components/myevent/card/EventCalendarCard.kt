package com.edts.components.myevent.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.edts.components.databinding.EventCalendarCardBinding
import com.edts.components.R

/**
 * A custom UI component that displays a calendar date in a card format.
 *
 * This view shows the month, day of the month, and day of the week, styled within a compact
 * MaterialCardView. It is designed to be easily integrated into layouts where a prominent
 * date display is needed, such as in event lists or appointment details.
 *
 * ```kotlin
 * val calendarCard = CustomCalendarCard(context)
 * calendarCard.setCalendarData(month = "AUG", dayOfMonth = "17", day = "Sat")
 * // Add the view to your layout
 * parentLayout.addView(calendarCard)
 * ```
 *
 */
class EventCalendarCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCalendarCardBinding

    /**
     * The three-letter abbreviation for the month (e.g., "JUL").
     * When set, the UI automatically updates.
     */
    var month: String? = null
        set(value) {
            field = value
            updateTexts()
        }

    /**
     * The numeric day of the month (e.g., "23").
     * When set, the UI automatically updates.
     */
    var date: String? = null
        set(value) {
            field = value
            updateTexts()
        }

    /**
     * The three-letter abbreviation for the day of the week (e.g., "Wed").
     * When set, the UI automatically updates.
     */
    var day: String? = null
        set(value) {
            field = value
            updateTexts()
        }

    init {
        binding = EventCalendarCardBinding.inflate(LayoutInflater.from(context), this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.EventCalendarCard,
                0,
                0)
            try {
                month = typedArray.getString(R.styleable.EventCalendarCard_month)
                date = typedArray.getString(R.styleable.EventCalendarCard_date)
                day = typedArray.getString(R.styleable.EventCalendarCard_day)
            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Updates the child TextViews with the current property values.
     * This method is called internally whenever a date property is changed.
     */
    private fun updateTexts() {
        binding.tvMonth.text = month
        binding.tvDate.text = date
        binding.tvDay.text = day
    }

    /**
     * Sets all calendar data properties at once for programmatic use.
     * This is more efficient than setting each property individually as it updates the UI only once.
     *
     * @param month The three-letter abbreviation for the month (e.g., "JUL").
     * @param dayOfMonth The numeric day of the month (e.g., "23").
     * @param day The three-letter abbreviation for the day of the week (e.g., "Wed").
     */
    fun setCalendarData(month: String, dayOfMonth: String, day: String) {
        this.month = month
        this.date = dayOfMonth
        this.day = day
    }
}