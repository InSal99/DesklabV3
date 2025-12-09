package com.edts.components.checkbox

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.edts.components.R

class CheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.checkboxStyle
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    private var normalTextAppearance = R.style.CheckBoxTextAppearance_Normal
    private var selectedTextAppearance = R.style.CheckBoxTextAppearance_Selected
    private var disabledTextAppearance = R.style.CheckBoxTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.CheckBoxTextAppearance_DisabledSelected
    private var errorTextAppearance = R.style.CheckBoxTextAppearance_Normal
    private var checkBoxDelegate: CheckboxDelegate? = null
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

        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.CustomCheckBox).apply {
                normalTextAppearance = getResourceId(
                    R.styleable.CustomCheckBox_checkboxNormalTextAppearance,
                    R.style.CheckBoxTextAppearance_Normal
                )
                selectedTextAppearance = getResourceId(
                    R.styleable.CustomCheckBox_checkboxSelectedTextAppearance,
                    R.style.CheckBoxTextAppearance_Selected
                )
                disabledTextAppearance = getResourceId(
                    R.styleable.CustomCheckBox_checkboxDisabledTextAppearance,
                    R.style.CheckBoxTextAppearance_Disabled
                )
                disabledSelectedTextAppearance = getResourceId(
                    R.styleable.CustomCheckBox_checkboxDisabledSelectedTextAppearance,
                    R.style.CheckBoxTextAppearance_DisabledSelected
                )
                isErrorState = getBoolean(R.styleable.CustomCheckBox_isCheckboxError, false)
                recycle()
            }
        }
    }

    private fun applyCustomStyle() {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_checkbox_states)
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
            animateCheckMark(checked)
        }

        updateTextAppearance()
    }

    private fun animateCheckMark(checked: Boolean) {
        currentAnimator?.cancel()

        val drawable = buttonDrawable
        val currentDrawable = if (drawable is StateListDrawable) {
            drawable.current
        } else {
            drawable
        }

        if (currentDrawable !is LayerDrawable || currentDrawable.numberOfLayers < 2) {
            return
        }

        val checkMarkDrawable = currentDrawable.getDrawable(1)
        if (checkMarkDrawable == null) {
            return
        }

        val originalBounds = Rect(checkMarkDrawable.bounds)
        val centerX = originalBounds.centerX()
        val centerY = originalBounds.centerY()
        val maxWidth = originalBounds.width()
        val maxHeight = originalBounds.height()

        val targetScale = if (checked) 1f else 0f
        val startScale = if (checked) 0f else 1f

        currentAnimator = ValueAnimator.ofFloat(startScale, targetScale).apply {
            duration = 200
            interpolator = android.view.animation.DecelerateInterpolator()

            addUpdateListener { animator ->
                val scale = animator.animatedValue as Float

                if (scale == 0f) {
                    checkMarkDrawable.setTint(ContextCompat.getColor(context, android.R.color.transparent))
                } else {
                    val tintColor = when {
                        !isEnabled -> ContextCompat.getColor(context, R.color.colorNeutralGrayDarkA20)
                        else -> ContextCompat.getColor(context, R.color.colorNeutralWhite)
                    }
                    checkMarkDrawable.setTint(tintColor)

                    val newWidth = (maxWidth * scale).toInt()
                    val newHeight = (maxHeight * scale).toInt()

                    val newLeft = centerX - newWidth / 2
                    val newTop = centerY - newHeight / 2
                    val newRight = centerX + newWidth / 2
                    val newBottom = centerY + newHeight / 2

                    checkMarkDrawable.setBounds(newLeft, newTop, newRight, newBottom)
                }

                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null

                    if (!checked) {
                        checkMarkDrawable.bounds = originalBounds
                        checkMarkDrawable.setTint(ContextCompat.getColor(context, android.R.color.transparent))
                    } else {
                        checkMarkDrawable.bounds = originalBounds
                        val tintColor = when {
                            !isEnabled -> ContextCompat.getColor(context, R.color.colorNeutralGrayDarkA20)
                            else -> ContextCompat.getColor(context, R.color.colorNeutralWhite)
                        }
                        checkMarkDrawable.setTint(tintColor)
                    }
                    invalidate()
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
        }

        currentAnimator?.start()
    }

    private fun updateTextAppearance() {
        val textAppearanceRes = when {
            !isEnabled && isChecked -> disabledSelectedTextAppearance
            !isEnabled -> disabledTextAppearance
            isChecked -> selectedTextAppearance
            isErrorState -> errorTextAppearance
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

    fun setErrorState(error: Boolean) {
        if (isErrorState != error) {
            isErrorState = error
            refreshDrawableState()
            updateTextAppearance()
        }
    }

    fun isErrorState(): Boolean = isErrorState


    fun setCustomCheckBoxDelegate(delegate: CheckboxDelegate?) {
        this.checkBoxDelegate = delegate
    }

    fun setTextAppearances(
        normal: Int = R.style.CheckBoxTextAppearance_Normal,
        selected: Int = R.style.CheckBoxTextAppearance_Selected,
        disabled: Int = R.style.CheckBoxTextAppearance_Disabled,
        disabledSelected: Int = R.style.CheckBoxTextAppearance_DisabledSelected,
        error: Int = R.style.CheckBoxTextAppearance_Normal
    ) {
        normalTextAppearance = normal
        selectedTextAppearance = selected
        disabledTextAppearance = disabled
        disabledSelectedTextAppearance = disabledSelected
        errorTextAppearance = error
        updateTextAppearance()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateTextAppearance()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isErrorState) {
            mergeDrawableStates(drawableState, STATE_ERROR)
        }
        return drawableState
    }

    override fun performClick(): Boolean {
        val result = super.performClick()
        setErrorState(false)

        checkBoxDelegate?.onCheckChanged(this, this.isChecked)
        return result
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        currentAnimator?.cancel()
        currentAnimator = null
    }

    companion object {
        private val STATE_ERROR = intArrayOf(R.attr.checkbox_state_error)
    }
}