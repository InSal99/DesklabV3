package com.edts.components.selection

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.SelectionDropdownFilterBinding
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


    private val colorCache = mutableMapOf<Int, Int>()

    var delegate: DropdownFilterDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    private companion object {
        const val TAG = "DropdownFilter"
    }

    init {
        radius = 999f * resources.displayMetrics.density
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

    private fun getCachedColor(@AttrRes colorAttr: Int): Int {
        return colorCache.getOrPut(colorAttr) {
            resolveColorAttribute(colorAttr)
        }
    }

    private fun updateCardBackground() {
        when (cardState) {
            CardState.REST -> {
                setCardBackgroundColor(getCachedColor(R.attr.colorBackgroundPrimary))
                val elevatedModifierDrawable = GradientDrawable().apply {
                    cornerRadius = 12f * resources.displayMetrics.density
                    setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated))
                }
                foreground = elevatedModifierDrawable
            }
            CardState.ON_PRESS -> {
                setCardBackgroundColor(getCachedColor(R.attr.colorBackgroundPrimary))
                val overlayDrawable = GradientDrawable().apply {
                    cornerRadius = 12f * resources.displayMetrics.density
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                foreground = overlayDrawable
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
                handleClick()
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
            Log.d(TAG, "  - Total system clicks: $clickCount")
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

    private fun resolveColorAttribute(colorRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(colorRes, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            try {
                ContextCompat.getColor(context, colorRes)
            } catch (e: Exception) {
                colorRes
            }
        }
    }

    fun resetClickCount() {
        val previousCount = clickCount
        clickCount = 0
        Log.d(TAG, "Click count reset from $previousCount to 0")
    }

    fun getClickCount(): Int {
        return clickCount
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "Programmatic click triggered")
        handleClick()
        return super.performClick()
    }
}