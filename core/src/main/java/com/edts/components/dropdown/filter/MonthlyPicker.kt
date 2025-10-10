package com.edts.components.dropdown.filter

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.edts.components.R
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

        val fallbackColorRes = android.R.color.transparent

        val textColor = when (type) {
            PickerType.UNSELECTED -> {
                val resolved = context.resolveColorAttribute(R.attr.colorForegroundPrimary, fallbackColorRes)
                try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
            }
            PickerType.SELECTED -> {
                val resolved = context.resolveColorAttribute(R.attr.colorForegroundPrimaryInverse, fallbackColorRes)
                try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
            }
            PickerType.DISABLED -> {
                val resolved = context.resolveColorAttribute(R.attr.colorForegroundDisabled, fallbackColorRes)
                try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
            }
        }
        monthLabelView.setTextColor(textColor)

        val styleKey = "${type}_${interactionState}_${radius}"
        val backgroundDrawable = drawableCache[styleKey] ?: createBackgroundDrawable().also {
            drawableCache[styleKey] = it
        }
        background = backgroundDrawable
    }

    private fun createBackgroundDrawable(): Drawable {
        val fallbackColorRes = android.R.color.transparent

        val (backgroundColor, strokeColor) = when (type) {
            PickerType.UNSELECTED -> Pair(
                Color.TRANSPARENT,
                run {
                    val resolved = context.resolveColorAttribute(R.attr.colorStrokeSubtle, fallbackColorRes)
                    try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
                }
            )
            PickerType.SELECTED -> Pair(
                run {
                    val resolved = context.resolveColorAttribute(R.attr.colorBackgroundAccentPrimaryIntense, fallbackColorRes)
                    try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
                },
                run {
                    val resolved = context.resolveColorAttribute(R.attr.colorStrokeInteractive, fallbackColorRes)
                    try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
                }
            )
            PickerType.DISABLED -> Pair(
                Color.TRANSPARENT,
                run {
                    val resolved = context.resolveColorAttribute(R.attr.colorStrokeDisabled, fallbackColorRes)
                    try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
                }
            )
        }

        val currentStrokeWidth = if (interactionState == InteractionState.ON_FOCUS) focusStrokeWidth else strokeWidth

        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@MonthlyPicker.radius
            setColor(backgroundColor)
            setStroke(currentStrokeWidth, strokeColor)
        }

        if (interactionState == InteractionState.ON_PRESS && type != PickerType.DISABLED) {
            val modifierColor = run {
                val resolved = context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, fallbackColorRes)
                try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
            }
            val modifierDrawable = GradientDrawable().apply {
                cornerRadius = this@MonthlyPicker.radius
                setColor(modifierColor)
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
}