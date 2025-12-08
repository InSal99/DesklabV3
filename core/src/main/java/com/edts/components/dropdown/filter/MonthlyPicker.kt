package com.edts.components.dropdown.filter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class MonthlyPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    enum class PickerType {
        UNSELECTED,
        SELECTED,
        DISABLED
    }

    private val monthLabelView: MaterialTextView
    private var monthLabel: String = ""
    private val strokeWidth: Int
    private val focusStrokeWidth: Int
    private val defaultCornerRadius: Float

    private val drawableCache = mutableMapOf<String, Drawable>()

    var delegate: MonthlyPickerDelegate? = null

    var type: PickerType = PickerType.UNSELECTED
        set(value) {
            if (field != value) {
                field = value
                updateStyling()
            }
        }

    init {
        strokeWidth = 1.dpToPx
        focusStrokeWidth = 2.dpToPx
        defaultCornerRadius = 12f.dpToPx

        monthLabelView = createMonthLabelView()
        addView(monthLabelView)

        parseAttributes(attrs)
        setupComponent()
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.MonthlyPicker, 0, 0) {
            monthLabel = getString(R.styleable.MonthlyPicker_monthLabel) ?: ""
            val typeInt = getInt(R.styleable.MonthlyPicker_pickerType, 0)
            type = when (typeInt) {
                1 -> PickerType.SELECTED
                2 -> PickerType.DISABLED
                else -> PickerType.UNSELECTED
            }
        }
    }

    private fun setupComponent() {
        cardElevation = 0f
        radius = defaultCornerRadius
        isClickable = true
        isFocusable = true

        setupRippleEffect()

        setOnClickListener {
            handleClick()
        }

        setMonthLabel(monthLabel)
        updateStyling()
    }

    private fun setupRippleEffect() {
        val rippleColor = context.resolveColorAttribute(
            R.attr.colorBackgroundModifierOnPress,
            R.color.colorNeutralGrayDarkA5
        )
        this.rippleColor = ColorStateList.valueOf(rippleColor)
    }

    private fun handleClick() {
        if (type == PickerType.DISABLED) return

        type = if (type == PickerType.SELECTED) {
            PickerType.UNSELECTED
        } else {
            PickerType.SELECTED
        }

        delegate?.onMonthClicked(this)
    }

    private fun updateStyling() {
        isEnabled = true
        isClickable = true
        isFocusable = (type != PickerType.DISABLED)

        updateTextColor()
        updateBackground()
    }

    private fun updateTextColor() {
        val textColor = when (type) {
            PickerType.UNSELECTED -> context.resolveColorAttribute(
                R.attr.colorForegroundPrimary,
                R.color.colorNeutralBlack
            )
            PickerType.SELECTED -> context.resolveColorAttribute(
                R.attr.colorForegroundPrimaryInverse,
                R.color.colorNeutralWhite
            )
            PickerType.DISABLED -> context.resolveColorAttribute(
                R.attr.colorForegroundDisabled,
                R.color.colorNeutralGrayDarkA20
            )
        }
        monthLabelView.setTextColor(textColor)
    }

    private fun updateBackground() {
        val styleKey = "${type}_${isPressed}_${isFocused}_${radius}"
        val backgroundDrawable = drawableCache[styleKey] ?: createBackgroundDrawable().also {
            drawableCache[styleKey] = it
        }
        background = backgroundDrawable
    }

    private fun createBackgroundDrawable(): Drawable {
        val (backgroundColor, strokeColor) = when (type) {
            PickerType.UNSELECTED -> Pair(
                Color.TRANSPARENT,
                context.resolveColorAttribute(
                    R.attr.colorStrokeSubtle,
                    R.color.colorNeutralGrayLight30
                )
            )
            PickerType.SELECTED -> Pair(
                context.resolveColorAttribute(
                    R.attr.colorBackgroundAccentPrimaryIntense,
                    R.color.colorBrandPrimary30
                ),
                context.resolveColorAttribute(
                    R.attr.colorStrokeInteractive,
                    R.color.colorNeutralGrayLightA20
                )
            )
            PickerType.DISABLED -> Pair(
                Color.TRANSPARENT,
                context.resolveColorAttribute(
                    R.attr.colorStrokeDisabled,
                    R.color.colorNeutralGrayDarkA12
                )
            )
        }

        val currentStrokeWidth = if (isFocused && type != PickerType.DISABLED) {
            focusStrokeWidth
        } else {
            strokeWidth
        }

        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@MonthlyPicker.radius
            setColor(backgroundColor)
            setStroke(currentStrokeWidth, strokeColor)
        }

        return baseDrawable
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updateBackground()
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun setMonthLabel(label: String) {
        this.monthLabel = label
        monthLabelView.text = label
    }

    private fun createMonthLabelView(): MaterialTextView {
        return MaterialTextView(context, null, 0).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            setTextAppearance(R.style.TextMedium_Label2)
            textAlignment = TEXT_ALIGNMENT_CENTER
            val verticalPadding = 12.dpToPx
            setPadding(0, verticalPadding, 0, verticalPadding)
        }
    }
}