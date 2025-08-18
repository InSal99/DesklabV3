package com.example.components.tab

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.components.R
import com.example.components.databinding.TabBinding

class Tab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: TabBinding = TabBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class TabState(val value: Int) {
        ACTIVE(0),
        INACTIVE(1);

        companion object {
            fun fromValue(value: Int): TabState {
                return values().find { it.value == value } ?: ACTIVE
            }
        }
    }

    var tabText: String? = null
        set(value) {
            field = value
            updateTabText()
        }

    var badgeText: String? = null
        set(value) {
            field = value
            updateBadgeText()
        }

    var showBadge: Boolean = true
        set(value) {
            field = value
            updateBadgeVisibility()
        }

    var tabState: TabState = TabState.ACTIVE
        set(value) {
            val previousState = field
            field = value
            updateTabState(animated = previousState != value)
        }

    private companion object {
        const val ANIMATION_DURATION = 150L
        const val INDICATOR_ANIMATION_DURATION = 200L
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Tab,
            0, 0
        ).apply {
            try {
                tabText = getString(R.styleable.Tab_tabText) ?: "Label"
                showBadge = getBoolean(R.styleable.Tab_tabShowBadge, true)

                val stateValue = getInt(R.styleable.Tab_tabState, 0)
                tabState = TabState.fromValue(stateValue)

                updateTabText()
                updateBadgeText()
                updateBadgeVisibility()
                updateTabState(animated = false)
            } finally {
                recycle()
            }
        }

        setOnClickListener {
            tabState = if (tabState == TabState.ACTIVE) TabState.INACTIVE else TabState.ACTIVE
        }
    }

    private fun updateTabText() {
        tabText?.let {
            binding.tvTab.text = it
        }
    }

    private fun updateBadgeText() {
        badgeText?.let {
            binding.cvBadgeTab.badgeText = it
        }
    }

    private fun updateBadgeVisibility() {
        binding.cvBadgeTab.visibility = if (showBadge) View.VISIBLE else View.GONE
    }

    private fun updateTabState(animated: Boolean = true) {
        if (animated) {
            animateTabStateChange()
        } else {
            applyTabStateImmediately()
        }
    }

    private fun applyTabStateImmediately() {
        when (tabState) {
            TabState.ACTIVE -> {
                val activeColor = getColorFromAttribute(R.attr.colorForegroundAccentPrimaryIntense)
                binding.tvTab.setTextColor(activeColor)
                binding.tabIndicator.visibility = View.VISIBLE
                binding.tabIndicator.scaleX = 1f
            }
            TabState.INACTIVE -> {
                val inactiveColor = getColorFromAttribute(R.attr.colorForegroundTertiary)
                binding.tvTab.setTextColor(inactiveColor)
                binding.tabIndicator.visibility = View.GONE
                binding.tabIndicator.scaleX = 0f
            }
        }
    }

    private fun animateTabStateChange() {
        when (tabState) {
            TabState.ACTIVE -> animateToActive()
            TabState.INACTIVE -> animateToInactive()
        }
    }

    private fun animateToActive() {
        val activeColor = getColorFromAttribute(R.attr.colorForegroundAccentPrimaryIntense)
        val currentTextColor = binding.tvTab.currentTextColor

        val textColorAnimator = ValueAnimator.ofArgb(currentTextColor, activeColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.tvTab.setTextColor(color)
            }
        }

        binding.tabIndicator.visibility = View.VISIBLE
        binding.tabIndicator.scaleX = 0f

        binding.tabIndicator.post {
            binding.tabIndicator.pivotX = binding.tabIndicator.width / 2f

            val indicatorAnimator = ObjectAnimator.ofFloat(binding.tabIndicator, "scaleX", 0f, 1f).apply {
                duration = INDICATOR_ANIMATION_DURATION
                interpolator = AccelerateDecelerateInterpolator()
            }

            AnimatorSet().apply {
                playTogether(textColorAnimator, indicatorAnimator)
                start()
            }
        }
    }

    private fun animateToInactive() {
        val inactiveColor = getColorFromAttribute(R.attr.colorForegroundTertiary)
        val currentTextColor = binding.tvTab.currentTextColor

        val textColorAnimator = ValueAnimator.ofArgb(currentTextColor, inactiveColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.tvTab.setTextColor(color)
            }
        }

        val indicatorAnimator = ObjectAnimator.ofFloat(binding.tabIndicator, "scaleX", 1f, 0f).apply {
            duration = INDICATOR_ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                if (animation.animatedFraction == 1f) {
                    binding.tabIndicator.visibility = View.GONE
                }
            }
        }

        AnimatorSet().apply {
            playTogether(textColorAnimator, indicatorAnimator)
            start()
        }
    }

    private fun getColorFromAttribute(attr: Int): Int {
        val typedValue = android.util.TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        return ContextCompat.getColor(context, typedValue.resourceId)
    }

}