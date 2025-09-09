package com.edts.components.myevent.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.EventCalendarCardBinding

/**
 * A custom UI component that displays a calendar date in a card format.
 *
 * This view shows the month, day of the month, and day of the week, styled within a compact
 * MaterialCardView. It is designed to be easily integrated into layouts where a prominent
 * date display is needed, such as in event lists or appointment details.
 *
 */
class EventCalendarCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCalendarCardBinding = EventCalendarCardBinding.inflate(LayoutInflater.from(context), this, true)

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
        parseAttributes(attrs)
    }

    /**
     * Parses custom attributes from the XML layout.
     * @param attrs The set of attributes from the XML tag that is inflating the view.
     */
    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.EventCalendarCard, 0, 0) {
                month = getString(R.styleable.EventCalendarCard_month)
                date = getString(R.styleable.EventCalendarCard_date)
                day = getString(R.styleable.EventCalendarCard_day)
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