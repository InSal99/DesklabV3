package com.edts.components.badge

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.edts.components.R
import com.edts.components.databinding.BadgeBinding

class Badge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: BadgeBinding = BadgeBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var badgeBackgroundTint: Int? = null
        set(value) {
            field = value
            updateBackgroundTint()
        }

    var badgeStrokeColor: Int? = null
        set(value) {
            field = value
            updateStrokeColor()
        }

    var badgeTextColor: Int? = null
        set(value) {
            field = value
            updateTextColor()
        }

    var badgeText: String? = null
        set(value) {
            field = value
            updateBadgeText()
        }

    var showText: Boolean = true
        set(value) {
            field = value
            updateTextVisibility()
        }

    init {
        // Parse custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Badge,
            0, 0
        ).apply {
            try {
                if (hasValue(R.styleable.Badge_badgeBackgroundTint)) {
                    badgeBackgroundTint = getColor(R.styleable.Badge_badgeBackgroundTint, 0)
                }
                if (hasValue(R.styleable.Badge_badgeStrokeColor)) {
                    badgeStrokeColor = getColor(R.styleable.Badge_badgeStrokeColor, 0)
                }
                if (hasValue(R.styleable.Badge_badgeTextColor)) {
                    badgeTextColor = getColor(R.styleable.Badge_badgeTextColor, 0)
                }
                badgeText = getString(R.styleable.Badge_badgeText)
                showText = getBoolean(R.styleable.Badge_showText, true)

                updateBackgroundTint()
                updateStrokeColor()
                updateTextColor()
                updateBadgeText()
                updateTextVisibility()
            } finally {
                recycle()
            }
        }
    }

    private fun updateBackgroundTint() {
        badgeBackgroundTint?.let { color ->
            binding.badgeCircle.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    private fun updateStrokeColor() {
        badgeStrokeColor?.let { color ->
            binding.badgeCircle.strokeColor = color
        }
    }

    private fun updateTextColor() {
        badgeTextColor?.let { color ->
            binding.tvBadge.setTextColor(color)
        }
    }

    private fun updateBadgeText() {
        badgeText?.let { text ->
            binding.tvBadge.text = text
        }
    }

    private fun updateTextVisibility() {
        binding.tvBadge.visibility = if (showText) View.VISIBLE else View.GONE
    }
}