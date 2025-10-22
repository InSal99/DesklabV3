package com.edts.components.sort.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.SortBtnBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class SortButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: SortBtnBinding = SortBtnBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val cornerRadiusPx = 12f.dpToPx

    enum class CardState {
        REST,
        ON_PRESS
    }

    private var cardState: CardState = CardState.REST
        set(value) {
            field = value
            updateCardBackground()
        }

    var delegate: SortButtonDelegate? = null

    var sortIcon: Int = R.drawable.ic_sort
        set(value) {
            field = value
            binding.ivFilterBTN.setImageResource(value)
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SortButton)

        try {
            val iconResId = typedArray.getResourceId(R.styleable.SortButton_sortIcon, R.drawable.ic_sort)
            sortIcon = iconResId
        } finally {
            typedArray.recycle()
        }

        setupCardPressState()
        radius = cornerRadiusPx
        rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)
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
                            R.attr.colorBackgroundModifierCardElevated,
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
                animateScaleDown()
                cardState = CardState.ON_PRESS
                return true
            }
            MotionEvent.ACTION_UP -> {
                cardState = CardState.REST
                animateScaleUp()
                handleClick()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                cardState = CardState.REST
                animateScaleUp()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleClick() {
        delegate?.onSortButtonClick(this)
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
        updateCardBackground()
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
}