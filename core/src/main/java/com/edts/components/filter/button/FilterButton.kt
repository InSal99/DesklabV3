package com.edts.components.filter.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.FilterBtnBinding
import com.google.android.material.card.MaterialCardView
import android.util.Log
import com.edts.components.event.card.EventCard.CardState

class FilterButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: FilterBtnBinding = FilterBtnBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val cornerRadiusPx = 12.toFloat() * context.resources.displayMetrics.density

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

    var delegate: FilterButtonDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    private companion object {
        const val TAG = "FilterButton"
    }

    init {
        setupCardPressState()
        radius = cornerRadiusPx
        rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)
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
                    setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated))
                }
                foreground = overlayDrawable
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "ACTION_DOWN - setting ON_PRESS state and scaling down")
                animateScaleDown()
                cardState = CardState.ON_PRESS
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP - setting REST state and scaling up")
                cardState = CardState.REST
                animateScaleUp()
                handleClick()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "ACTION_CANCEL - setting REST state and scaling up")
                cardState = CardState.REST
                animateScaleUp()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            Log.d(TAG, "FilterButton clicked!")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Current scale X: $scaleX")
            Log.d(TAG, "  - Current scale Y: $scaleY")
            Log.d(TAG, "  - Card state: $cardState")
            Log.d(TAG, "  - Total system clicks: $clickCount")
            Log.d(TAG, "--------------------")

            super.performClick()
            delegate?.onFilterButtonClick(this)
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
        Log.d(TAG, "Programmatic performClick() called")
        handleClick()
        return true
    }

    private fun animateScaleDown() {
        val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.95f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    private fun animateScaleUp() {
        val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1.0f)
        val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1.0f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY)
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    fun resetClickCount() {
        val previousCount = clickCount
        clickCount = 0
        Log.d(TAG, "Click count reset from $previousCount to 0")
    }

    fun getClickCount(): Int {
        return clickCount
    }

    fun simulateClick() {
        Log.d(TAG, "Simulated click triggered")
        animateScaleDown()
        cardState = CardState.ON_PRESS

        postDelayed({
            cardState = CardState.REST
            animateScaleUp()
            handleClick()
        }, 100)
    }

}