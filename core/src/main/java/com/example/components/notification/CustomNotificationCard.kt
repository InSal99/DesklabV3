package com.example.components.notification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.CustomNotificationCardBinding

/**
 * A custom view that displays a notification with a type, title, description, and an optional button.
 * The view's properties can be set via XML attributes or programmatically.
 */
class CustomNotificationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    // FIXED: Default style attribute now correctly points to materialCardViewStyle
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) { // FIXED: Class now extends MaterialCardView to match its XML root

    enum class EventType(val displayName: String) {
        GENERAL_EVENT("GENERAL EVENT"),
        PEOPLE_DEVELOPMENT("PEOPLE DEVELOPMENT"),
        EMPLOYEE_BENEFIT("EMPLOYEE BENEFIT");

        companion object {
            fun fromString(value: String?): EventType {
                return values().firstOrNull { it.displayName.equals(value, ignoreCase = true) } ?: GENERAL_EVENT
            }
        }
    }

    private val binding: CustomNotificationCardBinding

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
        get() = binding.btnNotification.visibility == VISIBLE
        set(value) {
            binding.btnNotification.visibility = if (value) VISIBLE else GONE
        }

    var eventType: EventType = EventType.GENERAL_EVENT
        set(value) {
            field = value
            updateEventTypeUI()
        }

    init {
        val inflater = LayoutInflater.from(context)
        // Since this class IS the MaterialCardView, we inflate the content and attach it.
        binding = CustomNotificationCardBinding.inflate(inflater, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomNotificationCard, 0, 0)

            title = typedArray.getString(R.styleable.CustomNotificationCard_notificationTitle) ?: ""
            description = typedArray.getString(R.styleable.CustomNotificationCard_notificationDescription) ?: ""
            buttonText = typedArray.getString(R.styleable.CustomNotificationCard_notificationButtonText) ?: "Confirm"
            isButtonVisible = typedArray.getBoolean(R.styleable.CustomNotificationCard_notificationButtonVisible, true)

            val eventTypeString = typedArray.getString(R.styleable.CustomNotificationCard_notificationEventType)
            eventType = EventType.fromString(eventTypeString)

            typedArray.recycle()
        }
    }

    fun setButtonClickListener(listener: () -> Unit) {
        binding.btnNotification.setOnClickListener { listener() }
    }

    private fun updateEventTypeUI() {
        binding.tvNotificationType.text = eventType.displayName

        // FIXED: The 'when' block now sets the correct icon for each event type.
        when (eventType) {
            EventType.GENERAL_EVENT -> {
                binding.notificationIcon.setIcon(R.drawable.ic_notification_event)
            }
            EventType.PEOPLE_DEVELOPMENT -> {
                // Assuming you have an icon named 'ic_notification_people'
                binding.notificationIcon.setIcon(R.drawable.ic_notification_event)
            }
            EventType.EMPLOYEE_BENEFIT -> {
                // Assuming you have an icon named 'ic_notification_benefit'
                binding.notificationIcon.setIcon(R.drawable.ic_notification_event)
            }
        }
    }
}
