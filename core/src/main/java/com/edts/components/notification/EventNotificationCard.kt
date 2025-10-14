package com.edts.components.notification

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.EventNotificationCardBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class EventNotificationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventNotificationCardBinding =
        EventNotificationCardBinding.inflate(LayoutInflater.from(context), this, true)

    var eventNotificationCardDelegate: EventNotificationCardDelegate? = null

    var title: String? = null
        set(value) {
            field = value
            binding.tvNotificationTitle.text = value
        }

    var description: String? = null
        set(value) {
            field = value
            binding.tvNotificationDescription.text = value
        }

    var buttonText: String? = null
        set(value) {
            field = value
            binding.btnNotification.text = value
        }

    var isButtonVisible: Boolean = true
        set(value) {
            field = value
            binding.btnNotification.isVisible = value
        }

    var eventCategory: EventCategory = EventCategory.GENERAL_EVENT
        set(value) {
            field = value
            updateEventCategoryUI()
        }

    init {
        setupCardAppearance()
        applyStyledAttributes(attrs)
        isClickable = true
        isFocusable = true

        binding.btnNotification.setOnClickListener {
            Log.d("EventNotificationCard", "Confirm Button Clicked ✅")
            eventNotificationCardDelegate?.onButtonClick(this)
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        Log.d("EventNotificationCard", "CardView Clicked ✅")
        eventNotificationCardDelegate?.onCardClick(this)
        return true
    }

    private fun applyStyledAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.EventNotificationCard, 0, 0) {
            val eventCategoryValue = getInt(R.styleable.EventNotificationCard_notificationEventCategory, 0)

            title = getString(R.styleable.EventNotificationCard_notificationTitle)
            description = getString(R.styleable.EventNotificationCard_notificationDescription)
            buttonText = getString(R.styleable.EventNotificationCard_notificationButtonText) ?: "Terima Undangan"
            isButtonVisible = getBoolean(R.styleable.EventNotificationCard_notificationButtonVisible, true)
            eventCategory = EventCategory.fromValue(eventCategoryValue)
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = context.resolveColorAttribute(
            R.attr.colorStrokeSubtle,
            R.color.colorNeutral30
        )
        val cornerRadius = 12f.dpToPx
        val elevation = 1f.dpToPx
        val strokeWidth = 1.dpToPx

        radius = cornerRadius
        cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = context.resolveColorAttribute(
                R.attr.colorForegroundPrimary,
                R.color.color000
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }

    private fun updateEventCategoryUI() {
        binding.tvNotificationType.text = eventCategory.displayText
        when (eventCategory) {
            EventCategory.GENERAL_EVENT, EventCategory.PEOPLE_DEVELOPMENT, EventCategory.EMPLOYEE_BENEFIT -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_event)
            }
        }
    }

    enum class EventCategory(val value: Int, val displayText: String) {
        GENERAL_EVENT(0, "GENERAL EVENT"),
        PEOPLE_DEVELOPMENT(1, "PEOPLE DEVELOPMENT"),
        EMPLOYEE_BENEFIT(2, "EMPLOYEE BENEFIT");

        companion object {
            fun fromValue(value: Int): EventCategory {
                return values().find { it.value == value } ?: GENERAL_EVENT
            }
        }
    }
}