package com.edts.components.checkbox

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
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
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_checkbox)
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

    override fun performClick(): Boolean {
        Log.d("CustomCheckBox", "checkbox clicked")
        val result = super.performClick()
        setErrorState(false)

        checkBoxDelegate?.onCheckChanged(this, this.isChecked)
        return result
    }

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

    companion object {
        private val STATE_ERROR = intArrayOf(R.attr.checkbox_state_error)
    }
}