package com.example.components.checkbox

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.example.components.R

class CustomCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.checkboxStyle
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    private var normalTextAppearance = R.style.CheckBoxTextAppearance_Normal
    private var selectedTextAppearance = R.style.CheckBoxTextAppearance_Selected
    private var disabledTextAppearance = R.style.CheckBoxTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.CheckBoxTextAppearance_DisabledSelected

    init {
        setBackgroundResource(android.R.color.transparent)
        applyCustomStyle()
        post { updateTextAppearance() }

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

    private fun updateTextAppearance() {
        val textAppearanceRes = when {
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
        normal: Int = R.style.CheckBoxTextAppearance_Normal,
        selected: Int = R.style.CheckBoxTextAppearance_Selected,
        disabled: Int = R.style.CheckBoxTextAppearance_Disabled,
        disabledSelected: Int = R.style.CheckBoxTextAppearance_DisabledSelected
    ) {
        normalTextAppearance = normal
        selectedTextAppearance = selected
        disabledTextAppearance = disabled
        disabledSelectedTextAppearance = disabledSelected
        updateTextAppearance()
    }
}