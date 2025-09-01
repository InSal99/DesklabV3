package com.edts.components.chip

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.SelectionChipBinding

class Chip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: SelectionChipBinding = SelectionChipBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    interface OnChipStateChangeListener {
        fun onChipStateChanged(chip: Chip, newState: ChipState, oldState: ChipState)
    }

    private var onChipStateChangeListener: OnChipStateChangeListener? = null

    enum class ChipState(val value: Int) {
        ACTIVE(0),
        INACTIVE(1);

        companion object {
            fun fromValue(value: Int): ChipState {
                return values().find { it.value == value } ?: INACTIVE
            }
        }
    }

    var chipState: ChipState = ChipState.INACTIVE
        set(value) {
            val oldState = field
            field = value
            updateChipState()
            if (oldState != value) {
                onChipStateChangeListener?.onChipStateChanged(this, value, oldState)
            }
        }

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

    private var colorAnimator: ValueAnimator? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Chip,
            0, 0
        ).apply {
            try {
                val chipStateValue = getInt(R.styleable.Chip_chipState, 1)
                chipState = ChipState.fromValue(chipStateValue)

                chipText = getString(R.styleable.Chip_chipText)
                chipBadgeText = getString(R.styleable.Chip_chipBadgeText)
                chipShowIcon = getBoolean(R.styleable.Chip_chipShowIcon, true)
                chipShowBadge = getBoolean(R.styleable.Chip_chipShowBadge, true)

                val iconResId = getResourceId(R.styleable.Chip_chipIcon, -1)
                if (iconResId != -1) {
                    chipIcon = iconResId
                }

                updateChipState()
                updateChipText()
                updateChipBadgeText()
                updateChipIconVisibility()
                updateChipBadgeVisibility()
                updateChipIcon()
            } finally {
                recycle()
            }
        }

        binding.chip.setOnClickListener {
            toggleChipState()
        }
    }

    private fun toggleChipState() {
        chipState = when (chipState) {
            ChipState.ACTIVE -> ChipState.INACTIVE
            ChipState.INACTIVE -> ChipState.ACTIVE
        }
    }

    private fun updateChipState() {
        animateStateChange()
    }

    private fun animateStateChange() {
        colorAnimator?.cancel()

        val typedValue = TypedValue()

        val currentBackgroundColor = when (chipState) {
            ChipState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundModifierCardElevated, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
            ChipState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundPrimaryInverse, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        }

        val currentTextColor = when (chipState) {
            ChipState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundPrimary, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
            ChipState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundPrimaryInverse, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        }

        val targetBackgroundColor = when (chipState) {
            ChipState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundPrimaryInverse, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
            ChipState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundModifierCardElevated, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        }

        val targetTextColor = when (chipState) {
            ChipState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundPrimaryInverse, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
            ChipState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundPrimary, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        }

        colorAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 100
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Float

                val interpolatedBgColor = interpolateColor(currentBackgroundColor, targetBackgroundColor, progress)
                binding.chip.backgroundTintList = android.content.res.ColorStateList.valueOf(interpolatedBgColor)
                binding.chip.setCardBackgroundColor(interpolatedBgColor)

                val interpolatedTextColor = interpolateColor(currentTextColor, targetTextColor, progress)
                binding.tvChip.setTextColor(interpolatedTextColor)
                binding.ivChip.imageTintList = android.content.res.ColorStateList.valueOf(interpolatedTextColor)
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    applyFinalStateColors()
                }
            })
            start()
        }
    }

    private fun applyFinalStateColors() {
        val typedValue = TypedValue()

        when (chipState) {
            ChipState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundPrimaryInverse, typedValue, true)
                binding.chip.backgroundTintList = ContextCompat.getColorStateList(context, typedValue.resourceId)
                binding.chip.setCardBackgroundColor(ContextCompat.getColor(context, typedValue.resourceId))

                context.theme.resolveAttribute(R.attr.colorStrokeInteractive, typedValue, true)
                val strokeColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.chip.strokeColor = strokeColor
                binding.cvBadgeChip.badgeStrokeColor = strokeColor

                context.theme.resolveAttribute(R.attr.colorForegroundPrimaryInverse, typedValue, true)
                val foregroundInverseColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.tvChip.setTextColor(foregroundInverseColor)
                binding.ivChip.imageTintList = ContextCompat.getColorStateList(context, typedValue.resourceId)
            }
            ChipState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundModifierCardElevated, typedValue, true)
                binding.chip.backgroundTintList = ContextCompat.getColorStateList(context, typedValue.resourceId)

                context.theme.resolveAttribute(R.attr.colorBackgroundPrimary, typedValue, true)
                binding.chip.setCardBackgroundColor(ContextCompat.getColor(context, typedValue.resourceId))

                context.theme.resolveAttribute(R.attr.colorStrokeSubtle, typedValue, true)
                binding.chip.strokeColor = ContextCompat.getColor(context, typedValue.resourceId)

                binding.cvBadgeChip.badgeStrokeColor = ContextCompat.getColor(context, android.R.color.transparent)

                context.theme.resolveAttribute(R.attr.colorForegroundPrimary, typedValue, true)
                binding.tvChip.setTextColor(ContextCompat.getColor(context, typedValue.resourceId))

                context.theme.resolveAttribute(R.attr.colorForegroundSecondary, typedValue, true)
                binding.ivChip.imageTintList = ContextCompat.getColorStateList(context, typedValue.resourceId)
            }
        }
    }

    private fun interpolateColor(startColor: Int, endColor: Int, fraction: Float): Int {
        val startA = (startColor shr 24) and 0xff
        val startR = (startColor shr 16) and 0xff
        val startG = (startColor shr 8) and 0xff
        val startB = startColor and 0xff

        val endA = (endColor shr 24) and 0xff
        val endR = (endColor shr 16) and 0xff
        val endG = (endColor shr 8) and 0xff
        val endB = endColor and 0xff

        return ((startA + (fraction * (endA - startA))).toInt() shl 24) or
                ((startR + (fraction * (endR - startR))).toInt() shl 16) or
                ((startG + (fraction * (endG - startG))).toInt() shl 8) or
                (startB + (fraction * (endB - startB))).toInt()
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

    private fun animatePress(onAnimationEnd: (() -> Unit)? = null) {
        colorAnimator?.cancel()

        val typedValue = TypedValue()

        val currentColor = when (chipState) {
            ChipState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundPrimaryInverse, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
            ChipState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorBackgroundModifierCardElevated, typedValue, true)
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        }

        context.theme.resolveAttribute(R.attr.colorBackgroundModifierOnPress, typedValue, true)
        val pressColor = ContextCompat.getColor(context, typedValue.resourceId)

        colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, pressColor, currentColor).apply {
            duration = 100
            addUpdateListener { animator ->
                val animatedColor = animator.animatedValue as Int
                binding.chip.backgroundTintList = android.content.res.ColorStateList.valueOf(animatedColor)
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onAnimationEnd?.invoke()
                }
            })
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        colorAnimator?.cancel()
    }
}