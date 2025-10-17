package com.edts.components.selection

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.edts.components.R
import com.edts.components.databinding.SelectionChipBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
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

    var delegate: ChipDelegate? = null

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

                radius = 999.dpToPx.toFloat()
                rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))

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
                setupChipTouchListener()
            } finally {
                recycle()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupIconClickListener() {
        binding.ivChip.setOnClickListener { view ->
            handleIconClick()
        }

        binding.ivChip.isClickable = true
        binding.ivChip.isFocusable = true

        val rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))

        val rippleDrawable = RippleDrawable(rippleColor, null, null)
        rippleDrawable.radius = 9f.dpToPx.toInt()

        binding.ivChip.background = rippleDrawable
    }

    private fun handleIconClick() {
        delegate?.onChipIconClick(this)
    }

    fun setIconClickable(clickable: Boolean) {
        binding.ivChip.isClickable = clickable
        binding.ivChip.isFocusable = clickable

        if (!clickable) {
            binding.ivChip.setOnClickListener(null)
            binding.ivChip.setOnTouchListener(null)
            binding.ivChip.background = null
        } else {
            setupIconClickListener()
        }
    }

    private fun getActiveBackgroundColor(): Int {
        return customActiveBackgroundColor ?: context.resolveColorAttribute(R.attr.colorBackgroundPrimaryInverse, R.color.color000)
    }

    private fun setupPressState() {
        isClickable = true
        isFocusable = true
        updatePressState()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupChipTouchListener() {
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressState = PressState.ON_PRESS
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    pressState = PressState.REST
                }
            }
            false
        }
    }

    private fun updatePressState() {
        if (chipState == ChipState.ACTIVE) {
            binding.chip.strokeColor = if (pressState == PressState.ON_PRESS) {
                context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
            } else {
                context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20)
            }
        }
    }

    override fun performClick(): Boolean {
        toggleChipState()
        delegate?.onChipClick(this, chipState)
        return super.performClick()
    }

    private fun toggleChipState() {
        val previousState = chipState
        chipState = when (chipState) {
            ChipState.ACTIVE -> ChipState.INACTIVE
            ChipState.INACTIVE -> ChipState.ACTIVE
        }
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
                binding.chip.setCardBackgroundColor(
                    ColorStateList.valueOf(
                        context.resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF)
                    )
                )
                binding.chip.strokeColor = context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
                binding.tvChip.setTextColor(context.resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
                ImageViewCompat.setImageTintList(
                    binding.ivChip,
                    ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorForegroundSecondary, R.color.colorNeutral60))
                )
                binding.cvBadgeChip.badgeStrokeColor = ContextCompat.getColor(context, android.R.color.transparent)
            }

            ChipState.ACTIVE -> {
                binding.chip.setCardBackgroundColor(ColorStateList.valueOf(getActiveBackgroundColor()))
                binding.chip.strokeColor = if (pressState == PressState.ON_PRESS) {
                    context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
                } else {
                    context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20)
                }
                binding.tvChip.setTextColor(context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, R.color.colorFFF))
                ImageViewCompat.setImageTintList(
                    binding.ivChip,
                    ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, R.color.colorFFF))
                )
                binding.cvBadgeChip.badgeStrokeColor = context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20)
            }
        }

    }

    private fun animateToState(newState: ChipState) {
        if (isAnimatingToState(newState)) {
            return
        }

        cancelAllAnimations()

        val animationDuration = 200L
        var completedAnimations = 0
        val totalAnimations = 5

        val onAnimationComplete = {
            completedAnimations++
            if (completedAnimations >= totalAnimations) {
                updateChipStateImmediate()
            }
        }

        when (newState) {
            ChipState.INACTIVE -> {
                animateBackgroundColor(
                    getActiveBackgroundColor(),
                    context.resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF),
                    animationDuration,
                    onAnimationComplete
                )
                animateStrokeColor(
                    if (chipState == ChipState.ACTIVE && pressState == PressState.ON_PRESS) {
                        context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
                    } else {
                        context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20)
                    },
                    context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30),
                    animationDuration,
                    onAnimationComplete
                )
                animateTextColor(
                    context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, R.color.colorFFF),
                    context.resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000),
                    animationDuration,
                    onAnimationComplete
                )
                animateIconTint(
                    context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, R.color.colorFFF),
                    context.resolveColorAttribute(R.attr.colorForegroundSecondary, R.color.colorNeutral60),
                    animationDuration,
                    onAnimationComplete
                )
                animateBadgeStrokeColor(
                    context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20),
                    ContextCompat.getColor(context, android.R.color.transparent),
                    animationDuration,
                    onAnimationComplete
                )
            }

            ChipState.ACTIVE -> {
                animateBackgroundColor(
                    context.resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF),
                    getActiveBackgroundColor(),
                    animationDuration,
                    onAnimationComplete
                )
                animateStrokeColor(
                    context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30),
                    if (pressState == PressState.ON_PRESS) {
                        context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
                    } else {
                        context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20)
                    },
                    animationDuration,
                    onAnimationComplete
                )
                animateTextColor(
                    context.resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000),
                    context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, R.color.colorFFF),
                    animationDuration,
                    onAnimationComplete
                )
                animateIconTint(
                    context.resolveColorAttribute(R.attr.colorForegroundSecondary, R.color.colorNeutral60),
                    context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, R.color.colorFFF),
                    animationDuration,
                    onAnimationComplete
                )
                animateBadgeStrokeColor(
                    ContextCompat.getColor(context, android.R.color.transparent),
                    context.resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorNeutral70Opacity20),
                    animationDuration,
                    onAnimationComplete
                )
            }
        }
    }

    private fun isAnimatingToState(targetState: ChipState): Boolean {
        return (backgroundAnimator?.isRunning == true ||
                strokeAnimator?.isRunning == true ||
                textColorAnimator?.isRunning == true ||
                iconTintAnimator?.isRunning == true ||
                badgeStrokeAnimator?.isRunning == true) &&
                chipState == targetState
    }

    private fun animateBackgroundColor(fromColor: Int, toColor: Int, duration: Long, onComplete: (() -> Unit)? = null) {
        backgroundAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.chip.setCardBackgroundColor(color)
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete?.invoke()
                }
            })
            start()
        }
    }

    private fun animateStrokeColor(fromColor: Int, toColor: Int, duration: Long, onComplete: (() -> Unit)? = null) {
        strokeAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.chip.strokeColor = color
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete?.invoke()
                }
            })
            start()
        }
    }

    private fun animateTextColor(fromColor: Int, toColor: Int, duration: Long, onComplete: (() -> Unit)? = null) {
        textColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.tvChip.setTextColor(color)
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete?.invoke()
                }
            })
            start()
        }
    }

    private fun animateIconTint(fromColor: Int, toColor: Int, duration: Long, onComplete: (() -> Unit)? = null) {
        iconTintAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                ImageViewCompat.setImageTintList(binding.ivChip, ColorStateList.valueOf(color))
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete?.invoke()
                }
            })
            start()
        }
    }

    private fun animateBadgeStrokeColor(fromColor: Int, toColor: Int, duration: Long, onComplete: (() -> Unit)? = null) {
        badgeStrokeAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val color = animator.animatedValue as Int
                binding.cvBadgeChip.badgeStrokeColor = color
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete?.invoke()
                }
            })
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

    fun setChipState(newState: ChipState, fromUser: Boolean = false) {
        if (chipState != newState) {
            val previousState = chipState
            chipState = newState

            if (fromUser) {
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