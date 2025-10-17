package com.edts.components.notification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.edts.components.R
import com.edts.components.databinding.EventNotificationIconBinding
import androidx.core.content.withStyledAttributes

class NotificationIcon @JvmOverloads constructor(
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

    fun setIcon(drawableResId: Int) {
        binding.ivNotificationIcon.setImageResource(drawableResId)
    }
}