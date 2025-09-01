package com.edts.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.edts.components.databinding.CustomCalendarCardBinding

/**
 * A custom UI component that displays a calendar date in a card format.
 *
 * This view shows the month, day of the month, and day of the week, styled within a compact
 * MaterialCardView. It is designed to be easily integrated into layouts where a prominent
 * date display is needed, such as in event lists or appointment details.
 *
 * ### XML Usage Example:
 * You can declare the `CustomCalendarCard` in your layout file and set the date
 * properties using custom attributes.
 *
 * ```xml
 * <com.example.components.CustomCalendarCard
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * app:month="JUL"
 * app:date="23"
 * app:day="Wed"/>
 * ```
 *
 * ### Programmatic Usage Example:
 * You can also create and configure the card programmatically.
 *
 * ```kotlin
 * val calendarCard = CustomCalendarCard(context)
 * calendarCard.setCalendarData(month = "AUG", dayOfMonth = "17", day = "Sat")
 * // Add the view to your layout
 * parentLayout.addView(calendarCard)
 * ```
 *
 * @attr ref R.styleable.CustomCalendarCard_month The three-letter abbreviation for the month (e.g., "JUL").
 * @attr ref R.styleable.CustomCalendarCard_date The numeric day of the month (e.g., "23").
 * @attr ref R.styleable.CustomCalendarCard_day The three-letter abbreviation for the day of the week (e.g., "Wed").
 *
 */
class CustomCalendarCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomCalendarCardBinding

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
        // Inflate the component's layout using View Binding and attach it to this view.
        binding = CustomCalendarCardBinding.inflate(LayoutInflater.from(context), this, true)

        // Parse custom attributes from XML, if provided.
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.CustomCalendarCard,
                0,
                0)
            try {
                // Set properties using values from XML.
                // The custom setters will automatically call updateTexts().
                month = typedArray.getString(R.styleable.CustomCalendarCard_month)
                date = typedArray.getString(R.styleable.CustomCalendarCard_date)
                day = typedArray.getString(R.styleable.CustomCalendarCard_day)
            } finally {
                // Always recycle the TypedArray after use.
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
        // Set the backing fields directly to avoid triggering multiple UI updates via setters.
        this.month = month
        this.date = dayOfMonth
        this.day = day
    }
}