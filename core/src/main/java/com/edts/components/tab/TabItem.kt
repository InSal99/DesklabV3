package com.edts.components.tab

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.edts.components.R
import com.edts.components.databinding.TabBinding
import com.edts.components.utils.resolveColorAttribute

class TabItem @JvmOverloads constructor(
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

    var tabState: TabState = TabState.INACTIVE
        set(value) {
            val previousState = field
            field = value

            if (isFirstLaunch) {
                applyTabStateImmediately()
                isFirstLaunch = false
            } else {
                updateTabState(animated = true)
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
            R.styleable.TabItem,
            0, 0
        ).apply {
            try {
                tabText = getString(R.styleable.TabItem_tabItemText) ?: "Label"
                showBadge = getBoolean(R.styleable.TabItem_tabItemShowBadge, true)
                badgeText = getString(R.styleable.TabItem_tabItemBadgeText)
                val stateValue = getInt(R.styleable.TabItem_tabItemState, 0)
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

    fun resetForBinding() {
        isFirstLaunch = true
    }

    private fun handleTabClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            val previousState = tabState

            Log.d(TAG, "Tab clicked!")
            Log.d(TAG, "  - Tab Text: ${tabText ?: "No text"}")
            Log.d(TAG, "  - Badge Text: ${badgeText ?: "No badge text"}")
            Log.d(TAG, "  - Show Badge: $showBadge")
            Log.d(TAG, "  - Current State: $previousState")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")

            if (delegate != null) {
                Log.d(TAG, "  - Has delegate, notifying parent component")
                delegate?.onTabClick(this, previousState, previousState)
            } else {
                val newState = if (tabState == TabState.ACTIVE) TabState.INACTIVE else TabState.ACTIVE
                Log.d(TAG, "  - Standalone TabItem, changing state to: $newState")
                tabState = newState
            }

            Log.d(TAG, "--------------------")
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
                val activeColor = context.resolveColorAttribute(
                    R.attr.colorForegroundAccentPrimaryIntense,
                    android.R.color.black
                )
                binding.tvTab.setTextColor(activeColor)
                binding.tabIndicator.visibility = View.VISIBLE
                binding.tabIndicator.scaleX = 1f
            }
            TabState.INACTIVE -> {
                val inactiveColor = context.resolveColorAttribute(
                    R.attr.colorForegroundTertiary,
                    android.R.color.darker_gray
                )
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
        val activeColor = context.resolveColorAttribute(
            R.attr.colorForegroundAccentPrimaryIntense,
            android.R.color.black
        )
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
        val inactiveColor = context.resolveColorAttribute(
            R.attr.colorForegroundTertiary,
            android.R.color.darker_gray
        )
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