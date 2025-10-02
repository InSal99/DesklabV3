package com.edts.components.event.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.EventCardBadgeBinding

class EventCardBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCardBadgeBinding = EventCardBadgeBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class BadgeSize(val value: Int) {
        SMALL(0),
        LARGE(1);

        companion object {
            fun fromValue(value: Int): BadgeSize {
                return values().find { it.value == value } ?: LARGE
            }
        }
    }

    enum class BadgeType(val value: Int) {
        LIVE(0),
        INVITED(1),
        REGISTERED(2),
        ATTENDED(3),
        NOTATTENDED(4);

        companion object {
            fun fromValue(value: Int): BadgeType {
                return values().find { it.value == value } ?: LIVE
            }
        }
    }

    var badgeType: BadgeType = BadgeType.LIVE
        set(value) {
            field = value
            updateBadgeColor()
        }

    var badgeSize: BadgeSize = BadgeSize.LARGE
        set(value) {
            field = value
            updateBadgeSize()
            updateBadgeColor()
        }

    var badgeText: String? = null
        set(value) {
            field = value
            updateBadgeText()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCardBadge,
            0, 0
        ).apply {
            try {
                val badgeTypeValue = getInt(R.styleable.EventCardBadge_eventBadgeType, 0)
                badgeType = BadgeType.fromValue(badgeTypeValue)
                val badgeSizeValue = getInt(R.styleable.EventCardBadge_badgeSize, 1)
                badgeSize = BadgeSize.fromValue(badgeSizeValue)
                badgeText = getString(R.styleable.EventCardBadge_eventCardBadgeText)

                updateBadgeSize()
                updateBadgeColor()
                updateBadgeText()
            } finally {
                recycle()
            }
        }
    }

    private fun updateBadgeColor() {
        val typedValue = android.util.TypedValue()
        val textColorTypedValue = android.util.TypedValue()

        when (badgeType) {
            BadgeType.LIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundAttentionIntense, typedValue, true)
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, typedValue.resourceId)

                context.theme.resolveAttribute(R.attr.colorForegroundPrimaryInverse, textColorTypedValue, true)
                binding.tvEvenCardBadgeLabel.setTextColor(ContextCompat.getColor(context, textColorTypedValue.resourceId))
            }
            BadgeType.INVITED -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundWarningIntense, typedValue, true)
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, typedValue.resourceId)

                context.theme.resolveAttribute(R.attr.colorForegroundPrimaryInverse, textColorTypedValue, true)
                binding.tvEvenCardBadgeLabel.setTextColor(ContextCompat.getColor(context, textColorTypedValue.resourceId))
            }
            BadgeType.REGISTERED -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundSuccessIntense, typedValue, true)
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, typedValue.resourceId)

                context.theme.resolveAttribute(R.attr.colorForegroundPrimaryInverse, textColorTypedValue, true)
                binding.tvEvenCardBadgeLabel.setTextColor(ContextCompat.getColor(context, textColorTypedValue.resourceId))
            }

            BadgeType.ATTENDED -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundTertiary, typedValue, true)
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, typedValue.resourceId)

                context.theme.resolveAttribute(R.attr.colorForegroundTertiary, textColorTypedValue, true)
                binding.tvEvenCardBadgeLabel.setTextColor(ContextCompat.getColor(context, textColorTypedValue.resourceId))

            }
            BadgeType.NOTATTENDED -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundTertiary, typedValue, true)
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, typedValue.resourceId)

                context.theme.resolveAttribute(R.attr.colorForegroundTertiary, textColorTypedValue, true)
                binding.tvEvenCardBadgeLabel.setTextColor(ContextCompat.getColor(context, textColorTypedValue.resourceId))
            }
        }
    }

    private fun updateBadgeSize() {
        when (badgeSize) {
            BadgeSize.SMALL -> {
                val typedValue = android.util.TypedValue()
                context.theme.resolveAttribute(R.attr.l3SemiBold, typedValue, true)
                binding.tvEvenCardBadgeLabel.setTextAppearance(typedValue.resourceId)

                binding.tvEvenCardBadgeLabel.setPadding(
                    resources.getDimensionPixelSize(R.dimen.margin_8dp),
                    resources.getDimensionPixelSize(R.dimen.margin_0dp),
                    resources.getDimensionPixelSize(R.dimen.margin_8dp),
                    resources.getDimensionPixelSize(R.dimen.margin_0dp)
                )
            }
            BadgeSize.LARGE -> {
                val typedValue = android.util.TypedValue()
                context.theme.resolveAttribute(R.attr.l3SemiBold, typedValue, true)
                binding.tvEvenCardBadgeLabel.setTextAppearance(typedValue.resourceId)

                binding.tvEvenCardBadgeLabel.setPadding(
                    resources.getDimensionPixelSize(R.dimen.margin_8dp),
                    resources.getDimensionPixelSize(R.dimen.margin_2dp),
                    resources.getDimensionPixelSize(R.dimen.margin_8dp),
                    resources.getDimensionPixelSize(R.dimen.margin_2dp)
                )
            }
        }
    }

    private fun updateBadgeText() {
        badgeText?.let {
            binding.tvEvenCardBadgeLabel.text = it
        }
    }
}