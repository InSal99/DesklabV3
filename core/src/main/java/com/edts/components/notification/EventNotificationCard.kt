package com.edts.components.notification

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.EventNotificationCardBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import androidx.core.view.isVisible

class EventNotificationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {
    enum class EventType(val displayName: String) {
        GENERAL_EVENT("GENERAL EVENT"),
        PEOPLE_DEVELOPMENT("PEOPLE DEVELOPMENT"),
        EMPLOYEE_BENEFIT("EMPLOYEE BENEFIT");

        companion object {
            fun fromString(value: String?): EventType {
                return values().firstOrNull { it.displayName.equals(value, ignoreCase = true) }
                    ?: GENERAL_EVENT
            }
        }
    }

    private val binding: EventNotificationCardBinding = EventNotificationCardBinding.inflate(LayoutInflater.from(context), this, true)

    var delegate: EventNotificationCardDelegate? = null

    var title: String
        get() = binding.tvNotificationTitle.text.toString()
        set(value) {
            binding.tvNotificationTitle.text = value
        }

    var description: String
        get() = binding.tvNotificationDescription.text.toString()
        set(value) {
            binding.tvNotificationDescription.text = value
        }

    var buttonText: String
        get() = binding.btnNotification.text.toString()
        set(value) {
            binding.btnNotification.text = value
        }

    var isButtonVisible: Boolean
        get() = binding.btnNotification.isVisible
        set(value) {
            binding.btnNotification.visibility = if (value) VISIBLE else GONE
        }

    var eventType: EventType = EventType.GENERAL_EVENT
        set(value) {
            field = value
            updateEventTypeUI()
        }

    init {
        setupCardAppearance()

        isClickable = true
        isFocusable = true

        attrs?.let {
            context.withStyledAttributes(it, R.styleable.EventNotificationCard, 0, 0) {
                title = getString(R.styleable.EventNotificationCard_notificationTitle) ?: ""
                description =
                    getString(R.styleable.EventNotificationCard_notificationDescription) ?: ""
                buttonText =
                    getString(R.styleable.EventNotificationCard_notificationButtonText) ?: "Terima Undangan"
                isButtonVisible =
                    getBoolean(R.styleable.EventNotificationCard_notificationButtonVisible, true)

                val eventTypeString = getString(R.styleable.EventNotificationCard_notificationEventType)
                eventType = EventType.fromString(eventTypeString)
            }
        }

        binding.btnNotification.setOnClickListener {
            Log.d("EventNotificationCard", "Confirm Button Clicked ✅")
            delegate?.onButtonClick(this)
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)
        val cornerRadius = resources.getDimension(R.dimen.radius_12dp)
        val elevation = resources.getDimension(R.dimen.dimen_1dp)
        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)

        radius = cornerRadius
        cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

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

    override fun performClick(): Boolean {
        super.performClick()
        Log.d("EventNotificationCard", "CardView Clicked ✅")
        delegate?.onCardClick(this)
        return true
    }

    private fun updateEventTypeUI() {
        binding.tvNotificationType.text = eventType.displayName
        when (eventType) {
            EventType.GENERAL_EVENT, EventType.PEOPLE_DEVELOPMENT, EventType.EMPLOYEE_BENEFIT -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_event)
            }
        }
    }
}

