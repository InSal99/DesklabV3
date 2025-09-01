package com.edts.components.chip

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.SelectionDropdownFilterBinding

class DropdownFilter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: SelectionDropdownFilterBinding = SelectionDropdownFilterBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var normalBackgroundColor: Int = 0
    private var pressedBackgroundColor: Int = 0
    private var backgroundAnimator: ValueAnimator? = null

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


    init {
        initializeColors()
        setupClickAnimation()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DropdownFilter,
            0, 0
        ).apply {
            try {
                dropdownFilterLabel = getString(R.styleable.DropdownFilter_dropdownFilterLabel)
                dropdownFilterDesc = getString(R.styleable.DropdownFilter_dropdownFilterDesc)
                dropdownFilterBadgeText = getString(R.styleable.DropdownFilter_dropdownFilterBadgeText)

                val iconResId = getResourceId(R.styleable.DropdownFilter_dropdownFilterIcon, -1)
                if (iconResId != -1) {
                    dropdownFilterIcon = iconResId
                }

                dropdownFilterShowBadge = getBoolean(R.styleable.DropdownFilter_dropdownFilterShowBadge, true)
                dropdownFilterShowDesc = getBoolean(R.styleable.DropdownFilter_dropdownFilterShowDesc, true)

                updateLabel()
                updateDescription()
                updateBadgeText()
                updateIcon()
                updateBadgeVisibility()
                updateDescriptionVisibility()
            } finally {
                recycle()
            }
        }
    }

    private fun initializeColors() {
        val typedValue = TypedValue()

        context.theme.resolveAttribute(R.attr.colorBackgroundModifierCardElevated, typedValue, true)
        normalBackgroundColor = ContextCompat.getColor(context, typedValue.resourceId)

        context.theme.resolveAttribute(R.attr.colorBackgroundModifierOnPress, typedValue, true)
        pressedBackgroundColor = ContextCompat.getColor(context, typedValue.resourceId)
    }

    private fun setupClickAnimation() {
        // Set touch listener on the parent FrameLayout (this view) instead of the card
        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateBackgroundColor(normalBackgroundColor, pressedBackgroundColor)
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    animateBackgroundColor(pressedBackgroundColor, normalBackgroundColor)
                    if (event.action == MotionEvent.ACTION_UP) {
                        performClick()
                    }
                    true
                }
                else -> false
            }
        }

        // Make sure the view is clickable
        this.isClickable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun animateBackgroundColor(fromColor: Int, toColor: Int) {
        backgroundAnimator?.cancel()

        backgroundAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            duration = 150
            addUpdateListener { animator ->
                val animatedColor = animator.animatedValue as Int
                binding.dropdownFilter.setCardBackgroundColor(animatedColor)
            }
            start()
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
        dropdownFilterIcon?.let {
            binding.ivDropdownFilter.setImageResource(it)
        }
    }

    private fun updateBadgeVisibility() {
        binding.cvBadgeDropdownFilter.visibility = if (dropdownFilterShowBadge) View.VISIBLE else View.GONE
    }

    private fun updateDescriptionVisibility() {
        binding.tvDropdownFilterDesc.visibility =
            if (dropdownFilterShowDesc) View.VISIBLE else View.GONE
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        backgroundAnimator?.cancel()
    }
}