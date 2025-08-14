package com.example.components.event.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.components.R
import com.example.components.databinding.EventCardStatusBinding

class EventCardStatus @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCardStatusBinding = EventCardStatusBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class StatusType(val value: Int) {
        RSVP(0),
        REGISTERED(1),
        UNREGISTERED(2);

        companion object {
            fun fromValue(value: Int): StatusType {
                return values().find { it.value == value } ?: RSVP
            }
        }
    }

    var statusType: StatusType = StatusType.RSVP
        set(value) {
            field = value
            updateStatusColor()
        }

    var statusText: String? = null
        set(value) {
            field = value
            updateStatusText()
        }

    init {

        // Parse custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCardStatus,
            0, 0
        ).apply {
            try {
                val statusTypeValue = getInt(R.styleable.EventCardStatus_statusType, 0)
                statusType = StatusType.fromValue(statusTypeValue)
                statusText = getString(R.styleable.EventCardStatus_statusText)

                updateStatusColor()
                updateStatusText()
            } finally {
                recycle()
            }
        }
    }

    private fun updateStatusColor() {
        val typedValue = android.util.TypedValue()
        when (statusType) {
            StatusType.RSVP -> {
                context.theme.resolveAttribute(R.attr.colorForegroundWarningIntense, typedValue, true)
                binding.tvEvenCardStatus.setTextColor(
                    ContextCompat.getColor(context, typedValue.resourceId)
                )
            }
            StatusType.REGISTERED -> {
                context.theme.resolveAttribute(R.attr.colorForegroundSuccessIntense, typedValue, true)
                binding.tvEvenCardStatus.setTextColor(
                    ContextCompat.getColor(context, typedValue.resourceId)
                )
            }
            StatusType.UNREGISTERED -> {
                context.theme.resolveAttribute(R.attr.colorForegroundAttentionIntense, typedValue, true)
                binding.tvEvenCardStatus.setTextColor(
                    ContextCompat.getColor(context, typedValue.resourceId)
                )
            }
        }
    }

    private fun updateStatusText() {
        statusText?.let {
            binding.tvEvenCardStatus.text = it
        }
    }
}