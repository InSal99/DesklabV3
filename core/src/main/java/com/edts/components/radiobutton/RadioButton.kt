package com.edts.components.radiobutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import com.edts.components.R
import androidx.core.content.withStyledAttributes

class RadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.radioButtonStyle
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    private var normalTextAppearance = R.style.RadioTextAppearance_Normal
    private var selectedTextAppearance = R.style.RadioTextAppearance_Selected
    private var disabledTextAppearance = R.style.RadioTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.RadioTextAppearance_DisabledSelected
    private var errorTextAppearance = R.style.RadioTextAppearance_Normal
    private var radioChangedDelegate: RadioButtonDelegate? = null
    private var isErrorState: Boolean = false
    private var currentAnimator: ValueAnimator? = null

    init {
        setBackgroundResource(android.R.color.transparent)
        applyCustomStyle()
        post {
            updateTextAppearance()
            layoutParams = layoutParams.apply {
                height = resources.getDimensionPixelSize(R.dimen.margin_24dp)
            }
        }

        context.withStyledAttributes(attrs, R.styleable.CustomRadioButton) {
            isErrorState = getBoolean(R.styleable.CustomRadioButton_isRadioError, false)
        }
    }

    override fun performClick(): Boolean {
        Log.d("CustomRadioButton", "radio button clicked")
        setErrorState(false)
        radioChangedDelegate?.onCheckChanged(this, this.isChecked)
        return super.performClick()
    }

    private fun applyCustomStyle() {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_radio_button)
        if (drawable != null) {
            buttonDrawable = drawable
        }

        val padding = resources.getDimensionPixelSize(R.dimen.margin_8dp)
        setPaddingRelative(padding, 0, 0, 0)
    }

    override fun setChecked(checked: Boolean) {
        val wasChecked = isChecked
        super.setChecked(checked)

        if (wasChecked != checked && isShown && width > 0) {
            animateInnerCircle(checked)
        }

        updateTextAppearance()
    }

    private fun animateInnerCircle(checked: Boolean) {
        Log.d("RadioButton", "animateInnerCircle called - checked: $checked")

        currentAnimator?.cancel()

        val drawable = buttonDrawable
        Log.d("RadioButton", "buttonDrawable: $drawable")
        Log.d("RadioButton", "buttonDrawable type: ${drawable?.javaClass?.simpleName}")

        val currentDrawable = if (drawable is StateListDrawable) {
            drawable.current
        } else {
            drawable
        }

        Log.d("RadioButton", "currentDrawable: $currentDrawable")
        Log.d("RadioButton", "currentDrawable type: ${currentDrawable?.javaClass?.simpleName}")

        if (currentDrawable !is LayerDrawable) {
            Log.d("RadioButton", "Current drawable is not a LayerDrawable, cannot animate")
            return
        }

        Log.d("RadioButton", "LayerDrawable layers count: ${currentDrawable.numberOfLayers}")

        if (currentDrawable.numberOfLayers < 2) {
            Log.d("RadioButton", "LayerDrawable has less than 2 layers")
            return
        }

        val innerCircleDrawable = currentDrawable.getDrawable(1)
        Log.d("RadioButton", "innerCircleDrawable: $innerCircleDrawable")
        Log.d("RadioButton", "innerCircleDrawable type: ${innerCircleDrawable?.javaClass?.simpleName}")

        if (innerCircleDrawable == null) {
            Log.d("RadioButton", "Inner circle drawable is null")
            return
        }

        val targetScale = if (checked) 1f else 0f
        val startScale = if (checked) 0f else 1f
        val originalBounds = Rect(innerCircleDrawable.bounds)
        Log.d("RadioButton", "Original bounds: $originalBounds")

        val centerX = originalBounds.centerX()
        val centerY = originalBounds.centerY()
        val maxWidth = originalBounds.width()
        val maxHeight = originalBounds.height()

        Log.d("RadioButton", "Animation params - centerX: $centerX, centerY: $centerY, maxWidth: $maxWidth, maxHeight: $maxHeight")
        Log.d("RadioButton", "Scale from $startScale to $targetScale")

        currentAnimator = ValueAnimator.ofFloat(startScale, targetScale).apply {
            duration = 200
            interpolator = android.view.animation.DecelerateInterpolator()

            addUpdateListener { animator ->
                val scale = animator.animatedValue as Float

                val newWidth = (maxWidth * scale).toInt()
                val newHeight = (maxHeight * scale).toInt()

                val newLeft = centerX - newWidth / 2
                val newTop = centerY - newHeight / 2
                val newRight = centerX + newWidth / 2
                val newBottom = centerY + newHeight / 2

                Log.d("RadioButton", "Animation update - scale: $scale, newBounds: ($newLeft, $newTop, $newRight, $newBottom)")

                innerCircleDrawable.setBounds(newLeft, newTop, newRight, newBottom)
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    if (checked && innerCircleDrawable is GradientDrawable) {
                        innerCircleDrawable.setColor(ContextCompat.getColor(context, R.color.colorFFF))
                    }
                }

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                    if (!checked) {
                        innerCircleDrawable.setBounds(centerX, centerY, centerX, centerY)
                        Log.d("RadioButton", "Animation ended - unchecked state")
                    } else {
                        innerCircleDrawable.bounds = originalBounds
                        if (innerCircleDrawable is GradientDrawable) {
                            innerCircleDrawable.setColor(ContextCompat.getColor(context, R.color.colorFFF))
                        }
                        Log.d("RadioButton", "Animation ended - checked state")
                    }
                    invalidate()
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                    Log.d("RadioButton", "Animation cancelled")
                }
            })
        }

        currentAnimator?.start()
        Log.d("RadioButton", "Animation started")
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateTextAppearance()
    }

    fun setErrorState(error: Boolean) {
        if (isErrorState != error) {
            isErrorState = error
            refreshDrawableState()
            updateTextAppearance()
        }
    }

    fun isErrorState(): Boolean = isErrorState

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isErrorState) {
            mergeDrawableStates(drawableState, STATE_ERROR)
        }
        return drawableState
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        currentAnimator?.cancel()
        currentAnimator = null
    }

    private fun updateTextAppearance() {
        val textAppearanceRes = when {
            isErrorState -> errorTextAppearance
            !isEnabled && isChecked -> disabledSelectedTextAppearance
            !isEnabled -> disabledTextAppearance
            isChecked -> selectedTextAppearance
            else -> normalTextAppearance
        }

        if (textAppearanceRes != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(textAppearanceRes)
            } else {
                @Suppress("DEPRECATION")
                setTextAppearance(context, textAppearanceRes)
            }
        }
    }

    fun setTextAppearances(
        normal: Int = R.style.RadioTextAppearance_Normal,
        selected: Int = R.style.RadioTextAppearance_Selected,
        disabled: Int = R.style.RadioTextAppearance_Disabled,
        disabledSelected: Int = R.style.RadioTextAppearance_DisabledSelected,
        error: Int = R.style.RadioTextAppearance_Normal
    ) {
        normalTextAppearance = normal
        selectedTextAppearance = selected
        disabledTextAppearance = disabled
        disabledSelectedTextAppearance = disabledSelected
        errorTextAppearance = error
        updateTextAppearance()
    }

    companion object {
        private val STATE_ERROR = intArrayOf(R.attr.radio_state_error)
    }
}