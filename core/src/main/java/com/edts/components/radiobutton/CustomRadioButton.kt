package com.edts.components.radiobutton

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import com.edts.components.R
import androidx.core.content.withStyledAttributes

class CustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.radioButtonStyle
) : AppCompatRadioButton(context, attrs, defStyleAttr) {
    private var normalTextAppearance = R.style.RadioTextAppearance_Normal
    private var selectedTextAppearance = R.style.RadioTextAppearance_Selected
    private var disabledTextAppearance = R.style.RadioTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.RadioTextAppearance_DisabledSelected
    private var errorTextAppearance = R.style.RadioTextAppearance_Normal // Add error text appearance
    private var radioChangedDelegate: CustomRadioButtonDelegate? = null
    private var isErrorState: Boolean = false

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
        super.setChecked(checked)
        updateTextAppearance()
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
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