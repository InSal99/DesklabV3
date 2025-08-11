package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import com.google.android.material.radiobutton.MaterialRadioButton

//class CustomRadioButton @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = android.R.attr.radioButtonStyle
//) : MaterialRadioButton(context, attrs, defStyleAttr) {
//
//    enum class RadioVariant { NORMAL, PRIMARY }
//
//    private var radioVariant: RadioVariant = RadioVariant.NORMAL
//
//    init {
//        attrs?.let {
//            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomRadioButton)
//            val variantValue = typedArray.getInt(R.styleable.CustomRadioButton_radioVariant, 0)
//            radioVariant = if (variantValue == 1) RadioVariant.PRIMARY else RadioVariant.NORMAL
//            typedArray.recycle()
//        }
//
//        applyCustomStyle()
//    }
//
//    private fun applyCustomStyle() {
//        // Apply your custom drawable
//        buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_radio_button)
//        buttonTintList = null
//
//        // Apply text appearance based on state
//        updateTextAppearance()
//    }
//
//    private fun updateTextAppearance() {
//        val textAppearanceRes = when {
//            !isChecked -> R.style.RadioTextAppearance_Disabled
//            isChecked -> R.style.RadioTextAppearance_Selected
//            else -> R.style.RadioTextAppearance_Normal
//        }
//        setTextAppearance(textAppearanceRes)
//    }
//
//    override fun setChecked(checked: Boolean) {
//        super.setChecked(checked)
//        updateTextAppearance()
//    }
//
//    override fun setEnabled(enabled: Boolean) {
//        super.setEnabled(enabled)
//        updateTextAppearance()
//    }
//
//    fun setRadioVariant(variant: RadioVariant) {
//        this.radioVariant = variant
//        applyCustomStyle()
//    }
//}

class CustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.radioButtonStyle
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    init {
        // Initialize with proper text appearance
        updateTextAppearance()
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
            !isEnabled -> R.style.RadioTextAppearance_Disabled
            isChecked -> R.style.RadioTextAppearance_Selected
            else -> R.style.RadioTextAppearance_Normal
        }

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            @Suppress("DEPRECATION")
//            super.setTextAppearance(context, textAppearanceRes)
//        } else {
//            super.setTextAppearance(textAppearanceRes)
//        }

        // Force refresh the text appearance
        refreshDrawableState()
    }
}