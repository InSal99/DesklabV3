package com.edts.components.chip

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
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
            updateCardBackground()
        }

    var delegate: DropdownFilterDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    private companion object {
        const val TAG = "DropdownFilter"
    }

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
                rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)

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
            } finally {
                recycle()
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
        dropdownFilterIcon?.let {
            binding.ivDropdownFilter.setImageResource(it)
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

    private fun updateCardBackground() {
        when (cardState) {
            CardState.REST -> {
                setCardBackgroundColor(
                    context.resolveColorAttribute(
                        R.attr.colorBackgroundPrimary,
                        android.R.color.transparent
                    )
                )
                foreground = GradientDrawable().apply {
                    cornerRadius = 12f.dpToPx
                    setColor(
                        context.resolveColorAttribute(
                            R.attr.colorBackgroundModifierCardElevated,
                            android.R.color.transparent
                        )
                    )
                }
            }
            CardState.ON_PRESS -> {
                setCardBackgroundColor(
                    context.resolveColorAttribute(
                        R.attr.colorBackgroundPrimary,
                        android.R.color.transparent
                    )
                )
                foreground = GradientDrawable().apply {
                    cornerRadius = 12f.dpToPx
                    setColor(
                        context.resolveColorAttribute(
                            R.attr.colorBackgroundModifierOnPress,
                            android.R.color.transparent
                        )
                    )
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "ACTION_DOWN - setting ON_PRESS state")
                cardState = CardState.ON_PRESS
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP - setting REST state")
                cardState = CardState.REST
                performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "ACTION_CANCEL - setting REST state")
                cardState = CardState.REST
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            Log.d(TAG, "DropdownFilter clicked!")
            Log.d(TAG, "  - Label: ${dropdownFilterLabel ?: "No label"}")
            Log.d(TAG, "  - Description: ${dropdownFilterDesc ?: "No description"}")
            Log.d(TAG, "  - Badge Text: ${dropdownFilterBadgeText ?: "No badge text"}")
            Log.d(TAG, "  - Show Badge: $dropdownFilterShowBadge")
            Log.d(TAG, "  - Show Description: $dropdownFilterShowDesc")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "--------------------")

            delegate?.onDropdownFilterClick(this)
        } else {
            Log.d(TAG, "Click ignored due to debounce (too fast)")
        }
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
        updateCardBackground()
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "Programmatic click triggered")
        handleClick()
        return super.performClick()
    }
}