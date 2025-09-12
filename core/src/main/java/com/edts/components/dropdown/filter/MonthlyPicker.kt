package com.edts.components.dropdown.filter

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.edts.components.R
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

    enum class InteractionState {
        REST,
        ON_PRESS,
        ON_FOCUS
    }

    private val monthLabelView: MaterialTextView
    private var monthLabel: String = ""
    private val strokeWidth: Int
    private val focusStrokeWidth: Int
    private val defaultCornerRadius: Float

    private val colorCache = mutableMapOf<Int, Int>()
    private val drawableCache = mutableMapOf<String, Drawable>()

    var delegate: MonthlyPickerDelegate? = null

    var type: PickerType = PickerType.UNSELECTED
        set(value) {
            if (field != value) {
                field = value
                applyStyling()
            }
        }

    private var interactionState: InteractionState = InteractionState.REST
        set(value) {
            if (field != value) {
                field = value
                applyStyling()
            }
        }

    init {
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        focusStrokeWidth = strokeWidth * 2
        defaultCornerRadius = resources.getDimension(R.dimen.radius_12dp)

        monthLabelView = createMonthLabelView()
        addView(monthLabelView)

        preloadColors()
        parseAttributes(attrs)
        setupComponent()
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.MonthlyPicker, 0, 0) {
                monthLabel = getString(R.styleable.MonthlyPicker_monthLabel) ?: ""
                val typeInt = getInt(R.styleable.MonthlyPicker_pickerType, 0)
                type = when (typeInt) {
                    1 -> PickerType.SELECTED
                    2 -> PickerType.DISABLED
                    else -> PickerType.UNSELECTED
                }
            }
        }
    }

    private fun applyStyling() {
        isEnabled = (type != PickerType.DISABLED)

        val textColor = when (type) {
            PickerType.UNSELECTED -> getCachedColor(R.attr.colorForegroundPrimary)
            PickerType.SELECTED -> getCachedColor(R.attr.colorForegroundPrimaryInverse)
            PickerType.DISABLED -> getCachedColor(R.attr.colorForegroundDisabled)
        }
        monthLabelView.setTextColor(textColor)

        val styleKey = "${type}_${interactionState}_${radius}"
        val backgroundDrawable = drawableCache[styleKey] ?: createBackgroundDrawable().also {
            drawableCache[styleKey] = it
        }
        background = backgroundDrawable
    }

    private fun createBackgroundDrawable(): Drawable {
        val (backgroundColor, strokeColor) = when (type) {
            PickerType.UNSELECTED -> Pair(Color.TRANSPARENT, getCachedColor(R.attr.colorStrokeSubtle))
            PickerType.SELECTED -> Pair(getCachedColor(R.attr.colorBackgroundAccentPrimaryIntense), getCachedColor(
                R.attr.colorStrokeInteractive))
            PickerType.DISABLED -> Pair(Color.TRANSPARENT, getCachedColor(R.attr.colorStrokeDisabled))
        }

        val currentStrokeWidth = if (interactionState == InteractionState.ON_FOCUS) focusStrokeWidth else strokeWidth

        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@MonthlyPicker.radius
            setColor(backgroundColor)
            setStroke(currentStrokeWidth, strokeColor)
        }

        if (interactionState == InteractionState.ON_PRESS && type != PickerType.DISABLED) {
            val modifierDrawable = GradientDrawable().apply {
                cornerRadius = this@MonthlyPicker.radius
                setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
            }
            return LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
        }

        return baseDrawable
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        interactionState = if (gainFocus) InteractionState.ON_FOCUS else InteractionState.REST
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                interactionState = InteractionState.ON_PRESS
                return true
            }
            MotionEvent.ACTION_UP -> {
                type = if (type == PickerType.SELECTED) PickerType.UNSELECTED else PickerType.SELECTED
                interactionState = if (isFocused) InteractionState.ON_FOCUS else InteractionState.REST
                performClick()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                interactionState = if (isFocused) InteractionState.ON_FOCUS else InteractionState.REST
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        Log.d("CustomMonthlyPicker", "Monthly Picker Clicked ✅")
        if (!isEnabled) return false
        delegate?.onMonthClicked(this)
        return super.performClick()
    }

    fun setMonthLabel(label: String) {
        this.monthLabel = label
        monthLabelView.text = label
    }

    private fun setupComponent() {
        cardElevation = 0f
        radius = defaultCornerRadius
        isClickable = true
        isFocusable = true
        setMonthLabel(monthLabel)
        applyStyling()
    }

    private fun createMonthLabelView(): MaterialTextView {
        val style = R.style.TextMedium_Label2
        return MaterialTextView(context, null, 0).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
            setTextAppearance(style)
            textAlignment = TEXT_ALIGNMENT_CENTER
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.margin_12dp)
            setPadding(0, verticalPadding, 0, verticalPadding)
        }
    }

    private fun preloadColors() {
        val colorAttrs = mapOf(
            R.attr.colorForegroundPrimary to R.color.color000,
            R.attr.colorForegroundPrimaryInverse to android.R.color.white,
            R.attr.colorForegroundDisabled to R.color.color000Opacity20,
            R.attr.colorBackgroundAccentPrimaryIntense to R.color.colorPrimary30,
            R.attr.colorStrokeSubtle to R.color.colorNeutral30,
            R.attr.colorStrokeInteractive to R.color.colorOpacityWhite20,
            R.attr.colorStrokeDisabled to R.color.color000Opacity12,
            R.attr.colorBackgroundModifierOnPress to R.color.color000Opacity5
        )

        colorAttrs.forEach { (attr, fallback) ->
            colorCache[attr] = resolveColorAttribute(attr, fallback)
        }
    }

    private fun getCachedColor(@AttrRes attrRes: Int): Int {
        return colorCache[attrRes] ?: resolveColorAttribute(attrRes, android.R.color.transparent)
    }

    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColorRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                typedValue.data
            } else {
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        } else {
            Log.w("CustomMonthlyPicker", "Attribute '$attrRes' not found, using fallback color.")
            ContextCompat.getColor(context, fallbackColorRes)
        }
    }
}