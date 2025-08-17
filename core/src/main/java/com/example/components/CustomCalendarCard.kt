package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.databinding.CustomCalendarCardBinding

class CustomCalendarCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomCalendarCardBinding

    // --- Public Properties with Custom Setters ---

    var month: String? = null
        set(value) {
            field = value
            updateTexts()
        }

    var date: String? = null
        set(value) {
            field = value
            updateTexts()
        }

    var day: String? = null
        set(value) {
            field = value
            updateTexts()
        }

    init {
        // Inflate the layout using View Binding
        binding = CustomCalendarCardBinding.inflate(LayoutInflater.from(context), this, true)

        // Parse custom attributes from XML
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
                typedArray.recycle()
            }
        }
    }

    /**
     * Updates the text views with the current property values.
     */
    private fun updateTexts() {
        binding.tvMonth.text = month
        binding.tvDate.text = date
        binding.tvDay.text = day
    }

    /**
     * Set calendar data using string values.
     * This method is kept for backward compatibility or for cases where setting all at once is easier.
     * @param month e.g. "JUL"
     * @param date e.g. "23"
     * @param day e.g. "Wed"
     */
    fun setCalendarData(month: String, date: String, day: String) {
        this.month = month
        this.date = date
        this.day = day
    }
}