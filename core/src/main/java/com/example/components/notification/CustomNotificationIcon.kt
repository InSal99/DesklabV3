package com.example.components.notification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.CustomNotificationIconBinding

/**
 * A custom view that displays a circular icon within a card.
 * This is used by the CustomNotificationCard.
 */
class CustomNotificationIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomNotificationIconBinding

    init {
        // Inflate the layout for this component
        val inflater = LayoutInflater.from(context)
        binding = CustomNotificationIconBinding.inflate(inflater, this, true)

        // Apply custom attributes from XML, if any
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomNotificationIcon, 0, 0)

            // Get the icon resource from the custom attribute 'notificationIcon'
            val iconResId = typedArray.getResourceId(R.styleable.CustomNotificationIcon_notificationIcon, 0)
            if (iconResId != 0) {
                setIcon(iconResId)
            }

            typedArray.recycle()
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