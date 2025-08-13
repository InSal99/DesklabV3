package com.example.components.event.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.components.R
import com.example.components.databinding.EventCardBadgeBinding

class EventCardBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: EventCardBadgeBinding

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
        REGISTERED(2);

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
        }

    var badgeText: String? = null
        set(value) {
            field = value
            updateBadgeText()
        }

    init {
        binding = EventCardBadgeBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        // Parse custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCardBadge,
            0, 0
        ).apply {
            try {
                val badgeTypeValue = getInt(R.styleable.EventCardBadge_badgeType, 0)
                badgeType = BadgeType.fromValue(badgeTypeValue)
                val badgeSizeValue = getInt(R.styleable.EventCardBadge_badgeSize, 1)
                badgeSize = BadgeSize.fromValue(badgeSizeValue)
                badgeText = getString(R.styleable.EventCardBadge_badgeText)

                updateBadgeColor()
                updateBadgeSize()
                updateBadgeText()
            } finally {
                recycle()
            }
        }
    }

    private fun updateBadgeColor() {
        when (badgeType) {
            BadgeType.LIVE -> {
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.colorRed30)
            }
            BadgeType.INVITED -> {
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.colorOrange50)
            }
            BadgeType.REGISTERED -> {
                binding.EventCardBadge.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.colorGreen50)
            }
        }
    }

    private fun updateBadgeSize() {
        when (badgeSize) {
            BadgeSize.SMALL -> {
                val typedValue = android.util.TypedValue()
//                context.theme.resolveAttribute(R.attr.l4SemiBold, typedValue, true)
                binding.tvEvenCardBadgeLabel.setTextAppearance(typedValue.resourceId)
                binding.tvEvenCardBadgeLabel.setPadding(
                    resources.getDimensionPixelSize(R.dimen.margin_6dp), // paddingStart
                    resources.getDimensionPixelSize(R.dimen.margin_4dp), // paddingTop
                    resources.getDimensionPixelSize(R.dimen.margin_6dp), // paddingEnd
                    resources.getDimensionPixelSize(R.dimen.margin_4dp)  // paddingBottom
                )
            }
            BadgeSize.LARGE -> {
                val typedValue = android.util.TypedValue()
                context.theme.resolveAttribute(R.attr.p2SemiBold, typedValue, true)
                binding.tvEvenCardBadgeLabel.setTextAppearance(typedValue.resourceId)
                binding.tvEvenCardBadgeLabel.setPadding(
                    resources.getDimensionPixelSize(R.dimen.margin_8dp), // paddingStart
                    resources.getDimensionPixelSize(R.dimen.margin_2dp), // paddingTop
                    resources.getDimensionPixelSize(R.dimen.margin_8dp), // paddingEnd
                    resources.getDimensionPixelSize(R.dimen.margin_2dp)  // paddingBottom
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