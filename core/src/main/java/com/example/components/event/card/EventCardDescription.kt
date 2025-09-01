package com.example.components.event.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.EventCardDescriptionBinding

class EventCardDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCardDescriptionBinding = EventCardDescriptionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var eventTitle: String? = null
        set(value) {
            field = value
            updateEventTitle()
        }

    var eventDate: String? = null
        set(value) {
            field = value
            updateEventDate()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCardDescription,
            0, 0
        ).apply {
            try {
                eventTitle = getString(R.styleable.EventCardDescription_eventTitle)
                eventDate = getString(R.styleable.EventCardDescription_eventDate)

                updateEventTitle()
                updateEventDate()
            } finally {
                recycle()
            }
        }
    }

    private fun updateEventTitle() {
        eventTitle?.let {
            binding.tvEvenCardSTitle.text = it
        }
    }

    private fun updateEventDate() {
        eventDate?.let {
            binding.tvEvenCardDate.text = it
        }
    }
}