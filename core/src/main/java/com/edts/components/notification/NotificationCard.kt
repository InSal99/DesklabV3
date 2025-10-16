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

class NotificationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventNotificationCardBinding =
        EventNotificationCardBinding.inflate(LayoutInflater.from(context), this, true)

    var notificationCardDelegate: NotificationCardDelegate? = null

    var notificationTitle: String? = null
        set(value) {
            field = value
            binding.tvNotificationTitle.text = value
        }

    var notificationDescription: String? = null
        set(value) {
            field = value
            binding.tvNotificationDescription.text = value
        }

    var primaryButtonText: String? = null
        set(value) {
            field = value
            binding.btnNotification.text = value
        }

    var secondaryButtonText: String? = null
        set(value) {
            field = value
            binding.btnNegativeNotification.text = value
        }

    var isPrimaryButtonVisible: Boolean = true
        set(value) {
            field = value
            updateButtonVisibility()
        }

    var isSecondaryButtonVisible: Boolean = false
        set(value) {
            field = value
            updateButtonVisibility()
        }

    var notificationCategory: NotificationCategory = NotificationCategory.GENERAL_EVENT
        set(value) {
            field = value
            updateEventCategoryUI()
        }

    var isBadgeVisible: Boolean = true
        set(value) {
            field = value
            binding.notificationBadge.isVisible = value
        }

    init {
        setupCardAppearance()
        applyStyledAttributes(attrs)
        setupClickListeners()
        isClickable = true
        isFocusable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        notificationCardDelegate?.onCardClick(this)
        return true
    }

    private fun setupClickListeners() {
        binding.btnNotification.setOnClickListener {
            notificationCardDelegate?.onPrimaryButtonClick(this)
        }

        binding.btnNegativeNotification.setOnClickListener {
            notificationCardDelegate?.onSecondaryButtonClick(this)
        }
    }

    private fun applyStyledAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.EventNotificationCard, 0, 0) {
            val eventCategoryValue = getInt(
                R.styleable.EventNotificationCard_notificationCategory,
                0
            )

            notificationTitle = getString(R.styleable.EventNotificationCard_notificationTitle)
            notificationDescription = getString(R.styleable.EventNotificationCard_notificationDescription)
            primaryButtonText = getString(R.styleable.EventNotificationCard_notificationPrimaryButtonText)
                ?: "Terima Undangan"
            secondaryButtonText = getString(R.styleable.EventNotificationCard_notificationSecondaryButtonText)
                ?: "Tolak"
            isPrimaryButtonVisible = getBoolean(
                R.styleable.EventNotificationCard_showNotificationPrimaryButton,
                true
            )
            isSecondaryButtonVisible = getBoolean(
                R.styleable.EventNotificationCard_showNotificationSecondaryButton,
                false
            )
            isBadgeVisible = getBoolean(
                R.styleable.EventNotificationCard_notificationBadgeVisible,
                true
            )
            notificationCategory = NotificationCategory.fromValue(eventCategoryValue)
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

    private fun updateButtonVisibility() {
        binding.btnNotification.isVisible = isPrimaryButtonVisible
        binding.btnNegativeNotification.isVisible = isPrimaryButtonVisible && isSecondaryButtonVisible
    }

    private fun updateEventCategoryUI() {
        binding.tvNotificationType.text = notificationCategory.displayText
        when (notificationCategory) {
            NotificationCategory.GENERAL_EVENT,
            NotificationCategory.PEOPLE_DEVELOPMENT,
            NotificationCategory.EMPLOYEE_BENEFIT -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_event)
            }
            NotificationCategory.ACTIVITY -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_activity)
            }
            NotificationCategory.LEAVE -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_leave)
            }
            NotificationCategory.SPECIAL_WORK -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_special_work)
            }
            NotificationCategory.DELEGATION -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_delegation)
            }
        }
    }

    enum class NotificationCategory(val value: Int, val displayText: String) {
        GENERAL_EVENT(0, "GENERAL EVENT"),
        PEOPLE_DEVELOPMENT(1, "PEOPLE DEVELOPMENT"),
        EMPLOYEE_BENEFIT(2, "EMPLOYEE BENEFIT"),
        ACTIVITY(3, "AKTIVITAS"),
        LEAVE(4, "CUTI"),
        SPECIAL_WORK(5, "KERJA KHUSUS"),
        DELEGATION(6, "DELEGASI");

        companion object {
            fun fromValue(value: Int): NotificationCategory {
                return values().find { it.value == value } ?: GENERAL_EVENT
            }
        }
    }
}