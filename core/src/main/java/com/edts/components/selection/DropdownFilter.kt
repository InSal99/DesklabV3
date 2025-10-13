package com.edts.components.selection

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.edts.components.R
import com.edts.components.databinding.SelectionDropdownFilterBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class DropdownFilter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: SelectionDropdownFilterBinding = SelectionDropdownFilterBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var dropdownFilterLabel: String? = null
        set(value) {
            field = value
            updateLabel()
        }

    var dropdownFilterDesc: String? = null
        set(value) {
            field = value
            updateDescription()
        }

    var dropdownFilterBadgeText: String? = null
        set(value) {
            field = value
            updateBadgeText()
        }

    var dropdownFilterIcon: Int? = null
        set(value) {
            field = value
            updateIcon()
        }

    var dropdownFilterShowBadge: Boolean = true
        set(value) {
            field = value
            updateBadgeVisibility()
        }

    var dropdownFilterShowDesc: Boolean = true
        set(value) {
            field = value
            updateDescriptionVisibility()
        }

    enum class CardState {
        REST,
        ON_PRESS
    }

    private var cardState: CardState = CardState.REST
        set(value) {
            field = value
        }

    var delegate: DropdownFilterDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    init {
        radius = 999f.dpToPx
        isClickable = true
        isFocusable = true

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DropdownFilter,
            0, 0
        ).apply {
            try {
                rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))

                dropdownFilterLabel = getString(R.styleable.DropdownFilter_dropdownFilterLabel)
                dropdownFilterDesc = getString(R.styleable.DropdownFilter_dropdownFilterDesc)
                dropdownFilterBadgeText =
                    getString(R.styleable.DropdownFilter_dropdownFilterBadgeText)

                val iconResId = getResourceId(R.styleable.DropdownFilter_dropdownFilterIcon, -1)
                if (iconResId != -1) {
                    dropdownFilterIcon = iconResId
                }

                dropdownFilterShowBadge =
                    getBoolean(R.styleable.DropdownFilter_dropdownFilterShowBadge, true)
                dropdownFilterShowDesc =
                    getBoolean(R.styleable.DropdownFilter_dropdownFilterShowDesc, true)

                updateLabel()
                updateDescription()
                updateBadgeText()
                updateIcon()
                updateBadgeVisibility()
                updateDescriptionVisibility()
                setupCardPressState()
                updateWrapperWidth()
                setOnClickListener {
                    handleClick()
                }
            } finally {
                recycle()
            }

            post { updateWrapperWidth() }
        }
    }

    private fun updateWrapperWidth() {
        layoutParams?.let { params ->
            binding.wrapper.layoutParams = (binding.wrapper.layoutParams as ConstraintLayout.LayoutParams).apply {
                width = if (params.width == LayoutParams.MATCH_PARENT) {
                    0
                } else {
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                }
            }
        }
    }

    private fun updateLabel() {
        dropdownFilterLabel?.let {
            binding.tvDropdownFilterLabel.text = it
        }
    }

    private fun updateDescription() {
        dropdownFilterDesc?.let {
            binding.tvDropdownFilterDesc.text = it
        }
    }

    private fun updateBadgeText() {
        dropdownFilterBadgeText?.let {
            binding.cvBadgeDropdownFilter.badgeText = it
        }
    }

    private fun updateIcon() {
        binding.ivDropdownFilter.isClickable = true
        binding.ivDropdownFilter.isFocusable = true

        val rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))

        val rippleDrawable = RippleDrawable(rippleColor, null, null)
        rippleDrawable.radius = 10f.dpToPx.toInt()

        binding.ivDropdownFilter.background = rippleDrawable

        dropdownFilterIcon?.let {
            binding.ivDropdownFilter.setImageResource(it)
        }

        binding.ivDropdownFilter.setOnClickListener {
            handleIconClick()
        }
    }

    private fun handleIconClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            lastClickTime = currentTime
            delegate?.onDropdownFilterIconClick(this)
        }
    }

    private fun updateBadgeVisibility() {
        binding.cvBadgeDropdownFilter.visibility =
            if (dropdownFilterShowBadge) View.VISIBLE else View.GONE
    }

    private fun updateDescriptionVisibility() {
        binding.tvDropdownFilterDesc.visibility =
            if (dropdownFilterShowDesc) View.VISIBLE else View.GONE
    }

    private fun handleClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime
            delegate?.onDropdownFilterClick(this)
        }
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
    }

    override fun performClick(): Boolean {
        handleClick()
        return super.performClick()
    }
}