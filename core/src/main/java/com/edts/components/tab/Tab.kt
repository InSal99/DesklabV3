package com.edts.components.tab

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.TabBinding

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

    private var isFirstLaunch = true

    var tabState: TabState = TabState.ACTIVE
        set(value) {
            val previousState = field
            field = value

            if (isFirstLaunch) {
                applyTabStateImmediately()
                isFirstLaunch = false
            } else {
                updateTabState(animated = previousState != value)
            }
        }

    private companion object {
        const val ANIMATION_DURATION = 150L
        const val INDICATOR_ANIMATION_DURATION = 200L
        const val TAG = "Tab"
    }

    var delegate: TabDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 200L

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
            handleTabClick()
        }
    }

    private fun handleTabClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            val previousState = tabState
            val newState = if (tabState == TabState.ACTIVE) TabState.INACTIVE else TabState.ACTIVE

            Log.d(TAG, "Tab clicked!")
            Log.d(TAG, "  - Tab Text: ${tabText ?: "No text"}")
            Log.d(TAG, "  - Badge Text: ${badgeText ?: "No badge text"}")
            Log.d(TAG, "  - Show Badge: $showBadge")
            Log.d(TAG, "  - Previous State: $previousState")
            Log.d(TAG, "  - New State: $newState")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Total system clicks: $clickCount")
            Log.d(TAG, "--------------------")

            tabState = newState
            delegate?.onTabClick(this, newState, previousState)
        } else {
            Log.d(TAG, "Click ignored due to debounce (too fast)")
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
                val activeColor = resolveColorAttribute(R.attr.colorForegroundAccentPrimaryIntense)
                binding.tvTab.setTextColor(activeColor)
                binding.tabIndicator.visibility = View.VISIBLE
                binding.tabIndicator.scaleX = 1f
            }
            TabState.INACTIVE -> {
                val inactiveColor = resolveColorAttribute(R.attr.colorForegroundTertiary)
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
        val activeColor = resolveColorAttribute(R.attr.colorForegroundAccentPrimaryIntense)
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
        val inactiveColor = resolveColorAttribute(R.attr.colorForegroundTertiary)
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
        handleTabClick()
        return super.performClick()
    }

    fun setTabState(state: TabState, triggerDelegate: Boolean = false) {
        val previousState = tabState
        tabState = state

        if (triggerDelegate && previousState != state) {
            Log.d(TAG, "Tab state changed programmatically:")
            Log.d(TAG, "  - Tab Text: ${tabText ?: "No text"}")
            Log.d(TAG, "  - Previous State: $previousState")
            Log.d(TAG, "  - New State: $state")

            delegate?.onTabClick(this, state, previousState)
        }
    }
}