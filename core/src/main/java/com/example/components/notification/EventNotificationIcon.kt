package com.example.components.notification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.EventNotificationIconBinding
import androidx.core.content.withStyledAttributes

/**
 * A custom view that displays a circular icon within a card.
 * This is used by the CustomNotificationCard.
 */
class EventNotificationIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventNotificationIconBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = EventNotificationIconBinding.inflate(inflater, this, true)

        attrs?.let {
            context.withStyledAttributes(it, R.styleable.EventNotificationIcon, 0, 0) {
                val iconResId = getResourceId(R.styleable.EventNotificationIcon_notificationIcon, 0)
                if (iconResId != 0) {
                    setIcon(iconResId)
                }

            }
        }
    }

    /**
     * Public method to change the icon displayed.
     * @param drawableResId The resource ID of the new drawable for the icon.
     */
    fun setIcon(drawableResId: Int) {
        binding.ivNotificationIcon.setImageResource(drawableResId)
    }
}