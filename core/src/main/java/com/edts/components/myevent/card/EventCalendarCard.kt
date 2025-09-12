package com.edts.components.myevent.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.EventCalendarCardBinding

class EventCalendarCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCalendarCardBinding = EventCalendarCardBinding.inflate(LayoutInflater.from(context), this, true)

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
        parseAttributes(attrs)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.EventCalendarCard, 0, 0) {
                month = getString(R.styleable.EventCalendarCard_month)
                date = getString(R.styleable.EventCalendarCard_date)
                day = getString(R.styleable.EventCalendarCard_day)
            }
        }
    }

    private fun updateTexts() {
        binding.tvMonth.text = month
        binding.tvDate.text = date
        binding.tvDay.text = day
    }

    fun setCalendarData(month: String, dayOfMonth: String, day: String) {
        this.month = month
        this.date = dayOfMonth
        this.day = day
    }
}