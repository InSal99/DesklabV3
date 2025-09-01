package com.edts.components.event.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.edts.components.R
import com.edts.components.databinding.EventCardBannerBinding

class EventCardBanner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCardBannerBinding = EventCardBannerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var eventType: String? = null
        set(value) {
            field = value
            updateEventType()
        }

    var eventCategory: String? = null
        set(value) {
            field = value
            updateEventCategory()
        }

    init {

        // Parse custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCardBanner,
            0, 0
        ).apply {
            try {
                eventType = getString(R.styleable.EventCardBanner_eventType)
                eventCategory = getString(R.styleable.EventCardBanner_eventCategory)

                updateEventType()
                updateEventCategory()
            } finally {
                recycle()
            }
        }
    }

    private fun updateEventType() {
        eventType?.let {
            binding.tvEvenCardType.text = it
        }
    }

    private fun updateEventCategory() {
        eventCategory?.let {
            binding.tvEvenCardCategory.text = it
        }
    }
}