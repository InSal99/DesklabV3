package com.example.components

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat

class CustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.radioButtonStyle
) : AppCompatRadioButton(context, attrs, defStyleAttr) {
    private var normalTextAppearance = R.style.RadioTextAppearance_Normal
    private var selectedTextAppearance = R.style.RadioTextAppearance_Selected
    private var disabledTextAppearance = R.style.RadioTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.RadioTextAppearance_DisabledSelected

    init {
        setBackgroundResource(android.R.color.transparent)
        applyCustomStyle()
        post { updateTextAppearance() }
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
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
        normal: Int = R.style.RadioTextAppearance_Normal,
        selected: Int = R.style.RadioTextAppearance_Selected,
        disabled: Int = R.style.RadioTextAppearance_Disabled,
        disabledSelected: Int = R.style.RadioTextAppearance_DisabledSelected
    ) {
        normalTextAppearance = normal
        selectedTextAppearance = selected
        disabledTextAppearance = disabled
        disabledSelectedTextAppearance = disabledSelected
        updateTextAppearance()
    }
}