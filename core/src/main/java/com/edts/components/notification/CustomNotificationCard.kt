package com.edts.components.notification

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.edts.components.R
import com.edts.components.databinding.CustomNotificationCardBinding
import androidx.core.content.withStyledAttributes

/**
 * A custom view that displays a notification with a type, title, description, and an optional button.
 * The view's properties can be set via XML attributes or programmatically.
 */
class CustomNotificationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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

    /**
     * The delegate responsible for handling click events on this component.
     * Assign an object that implements [CustomNotificationCardDelegate] to receive callbacks.
     */
    var delegate: CustomNotificationCardDelegate? = null

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
        binding = CustomNotificationCardBinding.inflate(inflater, this, true)

        // Make the entire card clickable to trigger performClick()
        isClickable = true
        isFocusable = true

        attrs?.let {
            context.withStyledAttributes(it, R.styleable.CustomNotificationCard, 0, 0) {
                title = getString(R.styleable.CustomNotificationCard_notificationTitle) ?: ""
                description =
                    getString(R.styleable.CustomNotificationCard_notificationDescription) ?: ""
                buttonText = getString(R.styleable.CustomNotificationCard_notificationButtonText)
                    ?: "Terima Undangan"
                isButtonVisible =
                    getBoolean(R.styleable.CustomNotificationCard_notificationButtonVisible, true)
                val eventTypeString =
                    getString(R.styleable.CustomNotificationCard_notificationEventType)
                eventType = EventType.fromString(eventTypeString)
            }
        }

        // Set up the button's click listener to notify the delegate
        binding.btnNotification.setOnClickListener {
            Log.d("EventNotificationCard", "Confirm Button Clicked ✅")
            delegate?.onButtonClick(this)
        }
    }

    /**
     * Performs the click action for the entire card and notifies the delegate.
     */
    override fun performClick(): Boolean {
        Log.d("EventNotificationCard", "CardView Clicked ✅")
        super.performClick()
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