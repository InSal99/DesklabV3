package com.edts.components.chip

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.edts.components.R
import com.edts.components.databinding.SelectionChipBinding
import com.google.android.material.card.MaterialCardView

class Chip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: SelectionChipBinding = SelectionChipBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class ChipState(val value: Int) {
        ACTIVE(0),
        INACTIVE(1);

        companion object {
            fun fromValue(value: Int): ChipState {
                return values().find { it.value == value } ?: INACTIVE
            }
        }
    }

    enum class ChipSize(val value: Int) {
        SMALL(0),
        MEDIUM(1);

        companion object {
            fun fromValue(value: Int): ChipSize {
                return values().find { it.value == value } ?: SMALL
            }
        }
    }

    enum class PressState {
        REST,
        ON_PRESS
    }

    var chipState: ChipState = ChipState.INACTIVE
        set(value) {
            if (field != value) {
                field = value
                animateToState(value)
            }
        }

    var chipSize: ChipSize = ChipSize.SMALL
        set(value) {
            if (field != value) {
                field = value
                updateChipSize()
            }
        }

    private var pressState: PressState = PressState.REST
        set(value) {
            if (field != value) {
                field = value
                updatePressState()
            }
        }

    private val colorCache = mutableMapOf<Int, Int>()

    private var customActiveBackgroundColor: Int? = null

    var chipText: String? = null
        set(value) {
            field = value
            updateChipText()
        }

    var chipBadgeText: String? = null
        set(value) {
            field = value
            updateChipBadgeText()
        }

    var chipShowIcon: Boolean = true
        set(value) {
            field = value
            updateChipIconVisibility()
        }

    var chipShowBadge: Boolean = true
        set(value) {
            field = value
            updateChipBadgeVisibility()
        }

    var chipIcon: Int? = null
        set(value) {
            field = value
            updateChipIcon()
        }

    private var backgroundAnimator: ValueAnimator? = null
    private var strokeAnimator: ValueAnimator? = null
    private var textColorAnimator: ValueAnimator? = null
    private var iconTintAnimator: ValueAnimator? = null
    private var badgeStrokeAnimator: ValueAnimator? = null

    private companion object {
        const val TAG = "Chip"
    }

    var delegate: ChipDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    private var iconClickCount = 0
    private var lastIconClickTime = 0L

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Chip,
            0, 0
        ).apply {
            try {
                val chipStateValue = getInt(R.styleable.Chip_chipState, 1)
                chipState = ChipState.fromValue(chipStateValue)

                val chipSizeValue = getInt(R.styleable.Chip_chipSize, 0)
                chipSize = ChipSize.fromValue(chipSizeValue)

                radius = 999f * resources.displayMetrics.density
                rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)

                chipText = getString(R.styleable.Chip_chipText)
                chipBadgeText = getString(R.styleable.Chip_chipBadgeText)
                chipShowIcon = getBoolean(R.styleable.Chip_chipShowIcon, true)
                chipShowBadge = getBoolean(R.styleable.Chip_chipShowBadge, true)

                val iconResId = getResourceId(R.styleable.Chip_chipIcon, -1)
                if (iconResId != -1) {
                    chipIcon = iconResId
                }

                if (hasValue(R.styleable.Chip_chipActiveBackgroundColor)) {
                    customActiveBackgroundColor = getColor(R.styleable.Chip_chipActiveBackgroundColor, 0)
                }

                updateChipStateImmediate()
                updateChipSize()
                updateChipText()
                updateChipBadgeText()
                updateChipIconVisibility()
                updateChipBadgeVisibility()
                updateChipIcon()
                setupPressState()
                setupIconClickListener()
            } finally {
                recycle()
            }
        }
    }

    private fun setupIconClickListener() {
        binding.ivChip.setOnClickListener { view ->
            handleIconClick()
        }

        binding.ivChip.isClickable = true
        binding.ivChip.isFocusable = true

        binding.ivChip.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.alpha = 0.7f
                    false // Return false to allow the click to be processed
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    view.alpha = 1.0f
                    false
                }
                else -> false
            }
        }
    }

    private fun handleIconClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastIconClickTime > clickDebounceDelay) {
            iconClickCount++
            lastIconClickTime = currentTime

            Log.d(TAG, "Icon clicked!")
            Log.d(TAG, "  - Chip Text: ${chipText ?: "No text"}")
            Log.d(TAG, "  - Chip State: $chipState")
            Log.d(TAG, "  - Icon visible: ${chipShowIcon}")
            Log.d(TAG, "  - Total icon clicks: $iconClickCount")
            Log.d(TAG, "  - Icon click timestamp: $currentTime")
            Log.d(TAG, "--------------------")

            delegate?.onChipIconClick(this)
        } else {
            Log.d(TAG, "Icon click ignored due to debounce (too fast)")
        }
    }

    fun setIconClickable(clickable: Boolean) {
        binding.ivChip.isClickable = clickable
        binding.ivChip.isFocusable = clickable

        if (!clickable) {
            binding.ivChip.setOnClickListener(null)
            binding.ivChip.setOnTouchListener(null)
        } else {
            setupIconClickListener()
        }
    }

    fun resetIconClickCount() {
        val previousCount = iconClickCount
        iconClickCount = 0
        Log.d(TAG, "Icon '${chipText ?: "Unknown"}' click count reset from $previousCount to 0")
    }

    fun getIconClickCount(): Int {
        return iconClickCount
    }

    private fun getCachedColor(@AttrRes colorAttr: Int): Int {
        return colorCache.getOrPut(colorAttr) {
            resolveColorAttribute(colorAttr)
        }
    }

    private fun getActiveBackgroundColor(): Int {
        return customActiveBackgroundColor ?: getCachedColor(R.attr.colorBackgroundPrimaryInverse)
    }

    private fun setupPressState() {
        isClickable = true
        isFocusable = true
        updatePressState()
    }

    private fun updatePressState() {
        when (pressState) {
            PressState.REST -> {
                val elevatedModifierDrawable = GradientDrawable().apply {
                    cornerRadius = 999f * resources.displayMetrics.density
                    setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated))
                }
                foreground = elevatedModifierDrawable
            }
            PressState.ON_PRESS -> {
                val overlayDrawable = GradientDrawable().apply {
                    cornerRadius = 999f * resources.displayMetrics.density
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                foreground = overlayDrawable
            }
        }

        if (chipState == ChipState.ACTIVE) {
            binding.chip.strokeColor = if (pressState == PressState.ON_PRESS) {
                getCachedColor(R.attr.colorStrokeSubtle)
            } else {
                getCachedColor(R.attr.colorStrokeInteractive)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "ACTION_DOWN - setting ON_PRESS state")
                pressState = PressState.ON_PRESS
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP - setting REST state")
                pressState = PressState.REST
                if (isEnabled && isClickable) {
                    performClick()
                }
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "ACTION_CANCEL - setting REST state")
                pressState = PressState.REST
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            Log.d(TAG, "Chip clicked!")
            Log.d(TAG, "  - Text: ${chipText ?: "No text"}")
            Log.d(TAG, "  - Current State: $chipState")
            Log.d(TAG, "  - Size: $chipSize")
            Log.d(TAG, "  - Show Icon: $chipShowIcon")
            Log.d(TAG, "  - Show Badge: $chipShowBadge")
            Log.d(TAG, "  - Badge Text: ${chipBadgeText ?: "No badge text"}")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Total system clicks: $clickCount")

            toggleChipState()

            Log.d(TAG, "  - New State: $chipState")
            Log.d(TAG, "--------------------")

            delegate?.onChipClick(this, chipState)

            return super.performClick()
        } else {
            Log.d(TAG, "Chip click ignored due to debounce (too fast)")
            return false
        }
    }

    private fun toggleChipState() {
        val previousState = chipState
        chipState = when (chipState) {
            ChipState.ACTIVE -> ChipState.INACTIVE
            ChipState.INACTIVE -> ChipState.ACTIVE
        }
        Log.d(TAG, "Chip state toggled: $previousState -> $chipState")
    }

    private fun updateChipSize() {
        val horizontalPadding: Int
        val verticalPadding: Int

        when (chipSize) {
            ChipSize.SMALL -> {
                horizontalPadding = resources.getDimensionPixelSize(R.dimen.margin_8dp)
                verticalPadding = resources.getDimensionPixelSize(R.dimen.margin_4dp)
            }

            ChipSize.MEDIUM -> {
                horizontalPadding = resources.getDimensionPixelSize(R.dimen.margin_10dp)
                verticalPadding = resources.getDimensionPixelSize(R.dimen.margin_6dp)
            }
        }

        binding.wrapperChip.setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
    }

    private fun updateChipStateImmediate() {
        cancelAllAnimations()

        when (chipState) {
            ChipState.INACTIVE -> {
                binding.chip.setCardBackgroundColor(getCachedColor(R.attr.colorBackgroundPrimary))
                binding.chip.strokeColor = getCachedColor(R.attr.colorStrokeSubtle)
                binding.tvChip.setTextColor(getCachedColor(R.attr.colorForegroundPrimary))
                ImageViewCompat.setImageTintList(
                    binding.ivChip,
                    ColorStateList.valueOf(getCachedColor(R.attr.colorForegroundSecondary))
                )
                binding.cvBadgeChip.badgeStrokeColor = ContextCompat.getColor(context, android.R.color.transparent)
            }

            ChipState.ACTIVE -> {
                binding.chip.setCardBackgroundColor(getActiveBackgroundColor())
                binding.chip.strokeColor = if (pressState == PressState.ON_PRESS) {
                    getCachedColor(R.attr.colorStrokeSubtle)
                } else {
                    getCachedColor(R.attr.colorStrokeInteractive)
                }
                binding.tvChip.setTextColor(getCachedColor(R.attr.colorForegroundPrimaryInverse))
                ImageViewCompat.setImageTintList(
                    binding.ivChip,
                    ColorStateList.valueOf(getCachedColor(R.attr.colorForegroundPrimaryInverse))
                )
                binding.cvBadgeChip.badgeStrokeColor = getCachedColor(R.attr.colorStrokeInteractive)
            }
        }
    }

    private fun animateToState(newState: ChipState) {
        cancelAllAnimations()

        val animationDuration = 200L

        when (newState) {
            ChipState.INACTIVE -> {
                animateBackgroundColor(
                    getActiveBackgroundColor(),
                    getCachedColor(R.attr.colorBackgroundPrimary),
                    animationDuration
                )
                animateStrokeColor(
                    if (chipState == ChipState.ACTIVE && pressState == PressState.ON_PRESS) {
                        getCachedColor(R.attr.colorStrokeSubtle)
                    } else {
                        getCachedColor(R.attr.colorStrokeInteractive)
                    },
                    getCachedColor(R.attr.colorStrokeSubtle),
                    animationDuration
                )
                animateTextColor(
                    getCachedColor(R.attr.colorForegroundPrimaryInverse),
                    getCachedColor(R.attr.colorForegroundPrimary),
                    animationDuration
                )
                animateIconTint(
                    getCachedColor(R.attr.colorForegroundPrimaryInverse),
                    getCachedColor(R.attr.colorForegroundSecondary),
                    animationDuration
                )
                animateBadgeStrokeColor(
                    getCachedColor(R.attr.colorStrokeInteractive),
                    ContextCompat.getColor(context, android.R.color.transparent),
                    animationDuration
                )
            }

            ChipState.ACTIVE -> {
                animateBackgroundColor(
                    getCachedColor(R.attr.colorBackgroundPrimary),
                    getActiveBackgroundColor(),
                    animationDuration
                )
                animateStrokeColor(
                    getCachedColor(R.attr.colorStrokeSubtle),
                    if (pressState == PressState.ON_PRESS) {
                        getCachedColor(R.attr.colorStrokeSubtle)
                    } else {
                        getCachedColor(R.attr.colorStrokeInteractive)
                    },
                    animationDuration
                )
                animateTextColor(
                    getCachedColor(R.attr.colorForegroundPrimary),
                    getCachedColor(R.attr.colorForegroundPrimaryInverse),
                    animationDuration
                )
                animateIconTint(
                    getCachedColor(R.attr.colorForegroundSecondary),
                    getCachedColor(R.attr.colorForegroundPrimaryInverse),
                    animationDuration
                )
                animateBadgeStrokeColor(
                    ContextCompat.getColor(context, android.R.color.transparent),
                    getCachedColor(R.attr.colorStrokeInteractive),
                    animationDuration
                )
            }
        }
    }

    private fun animateBackgroundColor(fromColor: Int, toColor: Int, duration: Long) {
        backgroundAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.chip.setCardBackgroundColor(color)
            }
            start()
        }
    }

    private fun animateStrokeColor(fromColor: Int, toColor: Int, duration: Long) {
        strokeAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.chip.strokeColor = color
            }
            start()
        }
    }

    private fun animateTextColor(fromColor: Int, toColor: Int, duration: Long) {
        textColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.tvChip.setTextColor(color)
            }
            start()
        }
    }

    private fun animateIconTint(fromColor: Int, toColor: Int, duration: Long) {
        iconTintAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                ImageViewCompat.setImageTintList(binding.ivChip, ColorStateList.valueOf(color))
            }
            start()
        }
    }

    private fun animateBadgeStrokeColor(fromColor: Int, toColor: Int, duration: Long) {
        badgeStrokeAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.cvBadgeChip.badgeStrokeColor = color
            }
            start()
        }
    }

    private fun cancelAllAnimations() {
        backgroundAnimator?.cancel()
        strokeAnimator?.cancel()
        textColorAnimator?.cancel()
        iconTintAnimator?.cancel()
        badgeStrokeAnimator?.cancel()
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

    private fun updateChipText() {
        chipText?.let {
            binding.tvChip.text = it
        }
    }

    private fun updateChipBadgeText() {
        chipBadgeText?.let {
            binding.cvBadgeChip.badgeText = it
        }
    }

    private fun updateChipIconVisibility() {
        binding.ivChip.visibility = if (chipShowIcon) View.VISIBLE else View.GONE
    }

    private fun updateChipBadgeVisibility() {
        binding.cvBadgeChip.visibility = if (chipShowBadge) View.VISIBLE else View.GONE
    }

    private fun updateChipIcon() {
        chipIcon?.let {
            binding.ivChip.setImageResource(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAllAnimations()
    }

    fun resetClickCount() {
        val previousCount = clickCount
        clickCount = 0
        Log.d(TAG, "Chip '${chipText ?: "Unknown"}' click count reset from $previousCount to 0")
    }

    fun getClickCount(): Int {
        return clickCount
    }

    fun setChipState(newState: ChipState, fromUser: Boolean = false) {
        if (chipState != newState) {
            val previousState = chipState
            chipState = newState

            if (fromUser) {
                clickCount++
                Log.d(TAG, "Chip state changed programmatically by user:")
                Log.d(TAG, "  - Text: ${chipText ?: "No text"}")
                Log.d(TAG, "  - Previous State: $previousState")
                Log.d(TAG, "  - New State: $newState")
                Log.d(TAG, "  - Total clicks: $clickCount")
                Log.d(TAG, "--------------------")

                delegate?.onChipClick(this, newState)
            }
        }
    }

    fun setActiveBackgroundColor(color: Int) {
        customActiveBackgroundColor = color
        if (chipState == ChipState.ACTIVE) {
            updateChipStateImmediate()
        }
    }
}